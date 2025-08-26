package ru.ttk.slotsbe.backend.service;//package ru.ttk.slotsbe.backend.service;

import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.ttk.slotsbe.backend.dto.*;
import ru.ttk.slotsbe.backend.mapper.SlotMapper;
import ru.ttk.slotsbe.backend.model.*;
import ru.ttk.slotsbe.backend.repository.*;
import ru.ttk.slotsbe.backend.api.*;
import ru.ttk.slotsbe.backend.util.ExcelReportGenerator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Класс для выполнения функций rest сервисов (GET, POST, PATCH, DELETE)
 *
 * @author Аркадий Тихонов
 */
@Service
@Slf4j
public class SlotApiService implements SlotsApiDelegate {

    @Autowired
    private VSlotRepository vSlotRepository;

    @Autowired
    private VStoreRepository vStoreRepository;

    @Autowired
    private SlotTemplateRepository slotTemplateRepository;

    @Autowired
    private SlotRepository slotRepository;

    @Autowired
    private SlotMapper slotMapper;

    @Autowired
    private ClientUserRepository clientUserRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private ExcelUploadService excelUploadService;

    @Autowired
    private SlotStatusRepository slotStatusRepository;

    /**
     * Список слотов
     */
    @Override
    public ResponseEntity<List<SlotDto>> getSlotsByFilters(SlotSearchFilter slotSearchFilter) {
//      Формирование выборки из View по заданным фильтрам
        List<SlotDto> result =
                vSlotRepository
                        .findAllByFilter(slotSearchFilter.getnStoreIds(), slotSearchFilter.getnClientIds(),
                                slotSearchFilter.getnStatusId(), slotSearchFilter.getdDateBegin(), slotSearchFilter.getdDateEnd())
                        .stream()
                        .map(v -> slotMapper.fromViewToDto(v))
                        .collect(Collectors.toList());

        return ResponseEntity.ok(result);

    }

    /**
     * Установка для слота статуса и указание клиента
     */
    @Override
    public ResponseEntity<Void> reserveSlots(List<ModifiedSlotDto> modifiedSlotDtos) {

        for (ModifiedSlotDto slot : modifiedSlotDtos) {
            if (slotRepository.findById(slot.getnSlotId()).isPresent()) {
                Slot modifiedSlot = slotRepository.findById(slot.getnSlotId()).get();
                modifiedSlot.setNSlotId(slot.getnSlotId());
                modifiedSlot.setNClientId(slot.getnClientId());
                slotRepository.save(modifiedSlot);
            }
        }
        return ResponseEntity.noContent().build();
    }

    /**
     * POST /slots/download : Выгрузка слотов в Excel файл
     *
     * @param slotSearchFilter (optional)
     * @return Excel файл (status code 200)
     */

    public ResponseEntity<Resource> downloadSlotsToExcel(SlotSearchFilter slotSearchFilter) {

        //  Формирование выборки из View по заданным фильтрам
        List<VSlot> slots = vSlotRepository
                .findAllByFilter(slotSearchFilter.getnStoreIds(), slotSearchFilter.getnClientIds(),
                        slotSearchFilter.getnStatusId(), slotSearchFilter.getdDateBegin(), slotSearchFilter.getdDateEnd());

        // Генерируем Excel в памяти
        byte[] excelBytes = null;
        try {
            excelBytes = ExcelReportGenerator.generateExcel(slots);
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
     * @param ids (optional)
     * @return Пустой ответ (status code 200)
     */
    public ResponseEntity<List<String>> sendMailsToUsers(List<Long> ids) {
        List<String> messages = new ArrayList<>();
        // Выбираем список пользователей
        List<ClientUser> users = clientUserRepository.findAllByNUserIds(ids);

        // Выбираем список слотов на сегодняшний день и после для клиента, по заданному идентификатору пользователя
        for (ClientUser user : users) {
            List<VSlot> slots = vSlotRepository.findAllByNClientId(user.getNClientId());
            String emailTo = user.getVcEmail();
            // Генерируем Excel в памяти
            byte[] excelBytes = null;
            try {
                excelBytes = ExcelReportGenerator.generateExcel(slots);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            // 3. Отправляем email с вложением
            try {
                String result = emailService.sendEmailWithExcelAttachment(
                        emailTo,
                        "Customer Report",
                        "Please find attached the customer report.",
                        excelBytes,
                        "customers_report.xlsx"
                );
                messages.add(result);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }

        }
        return ResponseEntity.ok(messages);
    }

    /**
     * POST /slots/template/upload : Загрузка файла c шаблоном расписания
     *
     * @param file Файл для загрузки (optional)
     * @return Пустой ответ (status code 200)
     */
    public ResponseEntity<List<String>> slotsTemplateUpload(MultipartFile file) {//    @Override
        List<String> messages = excelUploadService.saveSlotTemplatesFromExcel(file);
        return ResponseEntity.ok(messages);
    }

    /**
     * POST /slots/ : формирование слотов на основе шаблона
     *
     * @param slotGenerateParams (optional)
     * @return Список сообщений о формировании слотов (status code 200)
     */
    public ResponseEntity<List<String>> generateSlots(SlotGenerateParams slotGenerateParams) {
        List<String> messages = new ArrayList<>();
        List<Slot> slots = new ArrayList<>();
        // Цикл по нефтебазам
        for (Long storeId : slotGenerateParams.getnStoreIds()) {
            if (vStoreRepository.findById(storeId).isPresent()) {  //  если по id найдена нефтебаза
                String storeCode = vStoreRepository.findById(storeId).get().getVcCode();
                // Выбираем строки шаблона по данной нефтебазе
                List<SlotTemplate> templates = slotTemplateRepository.findAllByStoreId(storeId);
                // Цикл по дням
                // Задание начальной и конечной даты
                LocalDate dateBegin = slotGenerateParams.getdDateBegin();
                LocalDate dateEnd = slotGenerateParams.getdDateEnd();
                // Цикл по дням
                for (LocalDate genDate = dateBegin; !genDate.isAfter(dateEnd); genDate = genDate.plusDays(1)) {
                    // Если на данную дату для данной нефтебазы нет резервированых слотов, то генерим
                    if (slotRepository.findReservedSlotsByStoreIdAndDate(storeId, genDate).isEmpty()) {
                        //  Удаляем слоты на данную дату для данной нефтебазы
                        slotRepository.deleteSlotsByStoreIdAndDate(storeId, genDate);
                        // генерим слоты на основе шаблона
                        for (SlotTemplate template : templates) {
                            Slot slot = new Slot();
                            slot.setNLoadingPointId(template.getNLoadingPointId());
                            slot.setDDate(genDate);
                            slot.setDStartTime(template.getDStartTime());
                            slot.setDEndTime(template.getDEndTime());
                            slot.setNStatusId(template.getNStatusId());
                            slots.add(slot);
                        }
                        messages.add("Для нефтебазы: " + storeCode + " на дату: " + genDate + " слоты добавлены");
                    } else {
                        messages.add("Для нефтебазы: " + storeCode + " на дату: " + genDate + " есть резервированные слоты. " +
                                " Слоты не добавлены");
                    }
                }
            }
        }
        slotRepository.saveAll(slots);
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


}
