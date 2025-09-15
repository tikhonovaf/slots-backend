package ru.ttk.slotsbe.backend.service.rest;//package ru.ttk.slotsbe.backend.service;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import ru.ttk.slotsbe.backend.dto.*;
import ru.ttk.slotsbe.backend.mapper.SlotMapper;
import ru.ttk.slotsbe.backend.model.*;
import ru.ttk.slotsbe.backend.record.ExcelAttachment;
import ru.ttk.slotsbe.backend.repository.*;
import ru.ttk.slotsbe.backend.api.*;
import ru.ttk.slotsbe.backend.service.email.ClientUsersEmailSendService;
import ru.ttk.slotsbe.backend.service.excel.ExcelGenerator;
import ru.ttk.slotsbe.backend.service.excel.ExcelUploadService;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Класс для выполнения функций rest сервисов (GET, POST, PATCH, DELETE)
 *
 * @author Аркадий Тихонов
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class SlotApiService implements SlotsApiDelegate {

    private final VSlotRepository vSlotRepository;
    private final VStoreRepository vStoreRepository;
    private final SlotTemplateRepository slotTemplateRepository;
    private final SlotRepository slotRepository;
    private final SlotMapper slotMapper;
    private final ClientUserRepository clientUserRepository;
    private final VClientRepository vClientRepository;
    private final ClientUsersEmailSendService emailService;
    private final ExcelUploadService excelUploadService;
    private final SlotStatusRepository slotStatusRepository;
    private final TemplateEngine templateEngine;
    private final UserService userService;

    public static final Long RESERVED = 2L; // название подбирайте по смыслу

    /**
     * Список слотов
     */
    @Override
    public ResponseEntity<List<SlotDto>> getSlotsByFilters(SlotSearchFilter slotSearchFilter) {
//      Формирование выборки из View по заданным фильтрам
//      Если роль = 3 (пользователь клиента, то выборка с ограничением мпо клиенту )
        ClientUser currentUser = userService.getCurrentUser();
        if (currentUser.getNRoleId() < 3) {
            List<SlotDto> result =
                    vSlotRepository
                            .findAllByFilter(slotSearchFilter.getnStoreIds(), slotSearchFilter.getnClientIds(),
                                    slotSearchFilter.getnStatusId(), slotSearchFilter.getdDateBegin(), slotSearchFilter.getdDateEnd())
                            .stream()
                            .map(slotMapper::fromViewToDto)
                            .toList();

            return ResponseEntity.ok(result);
        } else {
            if (slotSearchFilter.getnStatusId() == RESERVED) {
                List<SlotDto> result =
                        vSlotRepository
                                .findAllByFilter(slotSearchFilter.getnStoreIds(), slotSearchFilter.getnClientIds(),
                                        slotSearchFilter.getnStatusId(), slotSearchFilter.getdDateBegin(), slotSearchFilter.getdDateEnd())
                                .stream()
                                .map(slotMapper::fromViewToDto)
                                .toList();

                return ResponseEntity.ok(result);
            } else {
                Long nClientId = currentUser.getNClientId();
                List<SlotDto> result =
                        vSlotRepository
                                .findAllFreeAndByFilter(slotSearchFilter.getnStoreIds(), nClientId,
                                        slotSearchFilter.getdDateBegin(), slotSearchFilter.getdDateEnd())
                                .stream()
                                .map(slotMapper::fromViewToDto)
                                .toList();

                return ResponseEntity.ok(result);
            }
        }
    }

    /**
     * POST /slots/download : Выгрузка слотов в Excel файл
     *
     * @param slotSearchFilter (optional)
     * @return Excel файл (status code 200)
     */

    @Override
    public ResponseEntity<Resource> downloadSlotsToExcel(SlotSearchFilter slotSearchFilter) {

        //  Формирование выборки из View по заданным фильтрам
        List<VSlot> slots = vSlotRepository
                .findAllByFilter(slotSearchFilter.getnStoreIds(), slotSearchFilter.getnClientIds(),
                        slotSearchFilter.getnStatusId(), slotSearchFilter.getdDateBegin(), slotSearchFilter.getdDateEnd());

        // Генерируем Excel в памяти
        byte[] excelBytes = null;
        try {
            excelBytes = ExcelGenerator.generateExcelSlots(slots);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // Создаем ресурс для скачивания
        ByteArrayResource resource = new ByteArrayResource(excelBytes);

        // Формируем ответ
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=slots_export.xlsx")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .contentLength(excelBytes.length)
                .body(resource);
    }

    /**
     * PATCH /slots/mails : Отправка писем со слотами пользователям
     *
     * @param slotSendByEmailParams
     * @return Пустой ответ (status code 200)
     */
    @Override
    public ResponseEntity<List<String>> sendMailsToUsers(SlotSendByEmailParams slotSendByEmailParams) {
        List<String> messages = new ArrayList<>();
        // Выбираем список пользователей
        List<ClientUser> users = clientUserRepository.findAllByNUserIds(slotSendByEmailParams.getnUserIds());

        // Выбираем список слотов по заданным параметрам
        for (ClientUser user : users) {
            List<VSlot> slots = vSlotRepository.findAllByClientIdAndDate(
                    user.getNClientId(),
                    slotSendByEmailParams.getdDateBegin(),
                    slotSendByEmailParams.getdDateEnd());
            messages.add("Выборка слотов для пользователя c email: " + user.getVcEmail()
                    + " За период: " + slotSendByEmailParams.getdDateBegin() + " - " + slotSendByEmailParams.getdDateEnd() +
                    " Количество слотов: " + slots.size());
            String emailTo = user.getVcEmail();
            // Генерируем Excel в памяти
            byte[] excelBytes = null;
            try {
                excelBytes = ExcelGenerator.generateExcelSlots(slots);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            // 3. Отправляем email с вложением

            List<ExcelAttachment> excelFiles = List.of(
                    new ExcelAttachment(excelBytes, "Список слотов.xlsx")
            );

            // Генерация HTML из шаблона
            String subject = "Список зарезервированных клиентом слотов и свободных слотов";
            Context context = new Context();
            String clientName = user.getVcFirstName() + " " + user.getVcSecondName();

            context.setVariable("name", clientName);
            String htmlContent = templateEngine.process("email-template", context);

            String result = emailService.sendEmailToClientUserWithExcelAttachments(
                    emailTo,
                    subject,
                    htmlContent,
                    excelFiles
            );
            messages.add(result);
        }
        return ResponseEntity.ok(messages);
    }

    /**
     * POST /slots/template/upload : Загрузка файла c шаблоном расписания
     *
     * @param file Файл для загрузки (optional)
     * @return Пустой ответ (status code 200)
     */
    @Override
    public ResponseEntity<List<String>> slotsTemplateUpload(MultipartFile file) {//    @Override
        List<String> messages = null;
        try {
            messages = excelUploadService.saveSlotTemplatesFromExcel(file.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(messages);
    }

    /**
     * POST /slots/ : формирование слотов на основе шаблона
     *
     * @param slotGenerateParams (optional)
     * @return Список сообщений о формировании слотов (status code 200)
     */
    @Override
    public ResponseEntity<List<String>> generateSlots(SlotGenerateParams slotGenerateParams) {
        List<String> messages = new ArrayList<>();
        List<Slot> slotsToSave = new ArrayList<>();

        LocalDate dateBegin = slotGenerateParams.getdDateBegin();
        LocalDate dateEnd = slotGenerateParams.getdDateEnd();

        for (Long storeId : slotGenerateParams.getnStoreIds()) {
            Optional<VStore> optionalStore = vStoreRepository.findById(storeId);
            if (optionalStore.isEmpty()) {
                messages.add("Нефтебаза с ID " + storeId + " не найдена.");
                continue;
            }

            String storeCode = optionalStore.get().getVcCode();
            List<SlotTemplate> templates = slotTemplateRepository.findAllByStoreId(storeId);

            for (LocalDate genDate = dateBegin; !genDate.isAfter(dateEnd); genDate = genDate.plusDays(1)) {
                boolean hasReservedSlots = !slotRepository.findReservedSlotsByStoreIdAndDate(storeId, genDate).isEmpty();

                if (hasReservedSlots) {
                    messages.add("Для нефтебазы: " + storeCode + " на дату: " + genDate +
                            " есть резервированные слоты. Слоты не добавлены.");
                    continue;
                }

                slotRepository.deleteSlotsByStoreIdAndDate(storeId, genDate);

                for (SlotTemplate template : templates) {
                    Slot slot = new Slot();
                    slot.setNLoadingPointId(template.getNLoadingPointId());
                    slot.setDDate(genDate);
                    slot.setDStartTime(template.getDStartTime());
                    slot.setDEndTime(template.getDEndTime());
                    slot.setNStatusId(template.getNStatusId());
                    slotsToSave.add(slot);
                }

                messages.add("Для нефтебазы: " + storeCode + " на дату: " + genDate + " слоты добавлены.");
            }
        }

        if (!slotsToSave.isEmpty()) {
            slotRepository.saveAll(slotsToSave);
        }

        return ResponseEntity.ok(messages);
    }

    /**
     * Список статусов
     */
    @Override
    public ResponseEntity<List<SlotStatusDto>> getSlotStatuses() {
//      Формирование списка статусов
        List<SlotStatusDto> result =
                slotStatusRepository
                        .findAll()
                        .stream()
                        .map(v -> slotMapper.fromEntityToDto(v))
                        .collect(Collectors.toList());

        return ResponseEntity.ok(result);

    }

    /**
     * PATCH /slots/reserve : Резервирование слотов
     *
     * @param modifiedSlotDtos (optional)
     * @return Пустой ответ (status code 200)
     */
    @Override
    public ResponseEntity<List<String>> reserveSlots(List<@Valid ModifiedSlotDto> modifiedSlotDtos) {
        final Long STATUS_RESERVED = 2L;
        List<String> messages = new ArrayList<>();

        for (ModifiedSlotDto slotDto : modifiedSlotDtos) {
            Long slotId = slotDto.getnSlotId();
            Long clientId = slotDto.getnClientId();

            Optional<Slot> optionalSlot = slotRepository.findById(slotId);
            Optional<VClient> optionalClient = vClientRepository.findById(clientId);

            if (optionalSlot.isEmpty()) {
                messages.add("Слот с ID " + slotId + " не найден.");
                continue;
            }

            if (optionalClient.isEmpty()) {
                messages.add("Клиент с ID " + clientId + " не найден.");
                continue;
            }

            Slot slot = optionalSlot.get();
            slot.setNStatusId(STATUS_RESERVED);
            slot.setNClientId(clientId);
            slotRepository.save(slot);

            vSlotRepository.findById(slotId).ifPresent(vSlot -> {
                messages.add(String.format("Зарезервирован слот: %s - %s - %s - %s - %s",
                        vSlot.getVcStoreCode(),
                        vSlot.getDDate(),
                        vSlot.getDStartTime(),
                        vSlot.getDEndTime(),
                        vSlot.getVcClientCode()));
            });
        }

        if (messages.isEmpty()) {
            messages.add("Слоты не зарезервированы.");
        }

        return ResponseEntity.ok(messages);
    }

    /**
     * PATCH /slots/free : Снятие слотов с резерва
     *
     * @param modifiedSlotDtos (optional)
     * @return Пустой ответ (status code 200)
     */
    @Override
    public ResponseEntity<List<String>> freeSlots(List<@Valid ModifiedSlotDto> modifiedSlotDtos) {
        final Long STATUS_FREE = 1L;
        List<String> messages = new ArrayList<>();

        for (ModifiedSlotDto slotDto : modifiedSlotDtos) {
            Long slotId = slotDto.getnSlotId();

            Optional<Slot> optionalSlot = slotRepository.findById(slotId);
            if (optionalSlot.isEmpty()) {
                messages.add("Слот с ID " + slotId + " не найден.");
                continue;
            }

            Slot slot = optionalSlot.get();
            slot.setNStatusId(STATUS_FREE);
            slot.setNClientId(null);
            slotRepository.save(slot);

            vSlotRepository.findById(slotId).ifPresent(vSlot -> {
                String message = String.format("Снят с резерва слот: %s - %s - %s - %s",
                        vSlot.getVcStoreCode(),
                        vSlot.getDDate(),
                        vSlot.getDStartTime(),
                        vSlot.getDEndTime());
                messages.add(message);
            });
        }

        if (messages.isEmpty()) {
            messages.add("Слоты не зарезервированы.");
        }

        return ResponseEntity.ok(messages);
    }

    /**
     * POST /slots/reserve/upload : Загрузка файла с запросом от пользователя клиента (для отладки)
     *
     * @param id   ИД пользователя (required)
     * @param file Файл для загрузки (optional)
     * @return Список сообщений о загрузке (status code 200)
     */
    @Override
    public ResponseEntity<Resource> slotsReserveUpload(Long id, MultipartFile file) {
        byte[] excelBytes;

        try {
            excelBytes = excelUploadService.processClientReserveFromExcel(file.getInputStream());
            log.info("Excel файл успешно сгенерирован.");
        } catch (IOException e) {
            log.error("Ошибка при обработке Excel-файла", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        ByteArrayResource resource = new ByteArrayResource(excelBytes);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=slots_export.xlsx")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .contentLength(excelBytes.length)
                .body(resource);
    }

}
