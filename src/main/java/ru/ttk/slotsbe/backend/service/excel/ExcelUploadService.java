package ru.ttk.slotsbe.backend.service.excel;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import ru.ttk.slotsbe.backend.model.*;
import ru.ttk.slotsbe.backend.repository.*;
import ru.ttk.slotsbe.backend.security.Sha512PasswordEncoder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExcelUploadService {

    private final SlotRepository slotRepository;
    private final SlotTemplateRepository slotTemplateRepository;
    private final VLoadingPointRepository vLoadingPointRepository;
    private final SlotStatusRepository slotStatusRepository;
    private final VStoreRepository vStoreRepository;
    private final LoadingPointRepository loadingPointRepository;
    private final VClientRepository vClientRepository;
    private final ClientUserRepository clientUserRepository;
    private final Sha512PasswordEncoder sha512PasswordEncoder;

    public static final Long RESERVED = 2L;

    public List<String> saveSlotTemplatesFromExcel(InputStream is) {
        List<String> result = new ArrayList<>();
        Set<Long> storeIdsToDelete = new HashSet<>();
        List<SlotTemplate> templates = new ArrayList<>();

        try (Workbook workbook = new XSSFWorkbook(is)) {

            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0 || isRowEmpty(row)) continue;

                Optional<SlotTemplate> optionalTemplate = parseSlotTemplateRow(row, result, storeIdsToDelete);
                optionalTemplate.ifPresent(templates::add);
            }

            if (result.isEmpty()) {
                storeIdsToDelete.forEach(slotTemplateRepository::deleteAllByStoreId);
                slotTemplateRepository.saveAll(templates);
                result.add("Шаблон загружен успешно. Загружено " + templates.size() + " строк");
                log.info("Загружено {} строк", templates.size());
            } else {
                log.warn("Ошибки при загрузке Excel: {}", result);
            }

        } catch (Exception e) {
            log.error("Ошибка при обработке Excel-файла", e);
            throw new RuntimeException("Ошибка при обработке Excel-файла", e);
        }

        return result;
    }


    private Optional<SlotTemplate> parseSlotTemplateRow(Row row, List<String> result, Set<Long> storeIdsToDelete) {
        SlotTemplate template = new SlotTemplate();

        String storeCode = getCellStringValue(row.getCell(0));
        String loadingPointCode = getCellStringValue(row.getCell(1));

        if (storeCode.isEmpty()) {
            result.add("Строка " + (row.getRowNum() + 1) + ": не задан код нефтебазы.");
            return Optional.empty();
        }
        if (loadingPointCode.isEmpty()) {
            result.add("Строка " + (row.getRowNum() + 1) + ": не задан код пункта налива.");
            return Optional.empty();
        }

        List<VLoadingPoint> vLoadingPoints =
                vLoadingPointRepository.findAllByStoreCodeAndLoadingPointCode(storeCode, loadingPointCode);

        if (vLoadingPoints.isEmpty()) {
            result.add("Строка " + (row.getRowNum() + 1) + ": не найдена запись для код нефтебазы = " +
                    storeCode + ", код пункта налива = " + loadingPointCode);
            return Optional.empty();
        }

        VLoadingPoint point = vLoadingPoints.get(0);
        template.setNLoadingPointId(point.getNLoadingPointId());
        storeIdsToDelete.add(point.getNStoreId());

        LocalTime startTime = getTimeFromCell(row.getCell(2), "Время начала слота", row.getRowNum(), result);
        LocalTime endTime = getTimeFromCell(row.getCell(3), "Время окончания слота", row.getRowNum(), result);

        if (startTime != null) template.setDStartTime(startTime);
        if (endTime != null) template.setDEndTime(endTime);

        Long statusId = getStatusId(row.getCell(4), row.getRowNum(), result);
        template.setNStatusId(statusId);

        return Optional.of(template);
    }

    private Long getStatusId(Cell cell, int rowNum, List<String> result) {
        if (cell == null || cell.getCellType() != CellType.STRING) return 1L;

        String statusCode = cell.getStringCellValue().trim();
        return slotStatusRepository.findByVcCode(statusCode)
                .map(status -> status.getNStatusId())
                .orElseGet(() -> {
                    result.add("Строка " + (rowNum + 1) + ": некорректный код статуса слота.");
                    return 1L;
                });
    }

    public List<String> saveLoadingPointsFromExcel(InputStream is) {
        List<String> result = new ArrayList<>();
        Set<Long> storeIdsToDelete = new HashSet<>();
        List<LoadingPoint> loadingPoints = new ArrayList<>();

        try (Workbook workbook = new XSSFWorkbook(is)) {

            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0 || isRowEmpty(row)) continue;

                Optional<LoadingPoint> optionalLoadingPoint = parseLoadingPointRow(row, result, storeIdsToDelete);
                optionalLoadingPoint.ifPresent(loadingPoints::add);
            }

            if (result.isEmpty()) {
                storeIdsToDelete.forEach(loadingPointRepository::deleteAllByStoreId);
                loadingPointRepository.saveAll(loadingPoints);
                result.add("Шаблон загружен успешно. Загружено " + loadingPoints.size() + " строк");
                log.info("Загружено {} строк", loadingPoints.size());
            } else {
                log.warn("Ошибки при загрузке Excel: {}", result);
            }

        } catch (Exception e) {
            log.error("Ошибка при обработке Excel-файла", e);
            throw new RuntimeException("Ошибка при обработке Excel-файла", e);
        }

        return result;
    }

    private Optional<LoadingPoint> parseLoadingPointRow(Row row, List<String> result, Set<Long> storeIdsToDelete) {
        LoadingPoint loadingPoint = new LoadingPoint();

        String storeCode = getCellStringValue(row.getCell(0));

        if (storeCode.isEmpty()) {
            result.add("Строка " + (row.getRowNum() + 1) + ": не задан код нефтебазы.");
            return Optional.empty();
        }

        List<VStore> vStores =
                vStoreRepository.findAllByVcCode(storeCode);

        if (vStores.isEmpty()) {
            result.add("Строка " + (row.getRowNum() + 1) + ": не найдена нефтебаза с заданным кодом.");
            return Optional.empty();
        }
        VStore vStore = vStores.get(0);
        loadingPoint.setNStoreId(vStore.getNStoreId());
        loadingPoint.setVcCode(getCellStringValue(row.getCell(1)));
        loadingPoint.setVcName(getCellStringValue(row.getCell(2)));
        loadingPoint.setVcComment(getCellStringValue(row.getCell(3)));
        storeIdsToDelete.add(vStore.getNStoreId());
        return Optional.of(loadingPoint);
    }


    public List<String> saveClientUsersFromExcel(InputStream is) {
        List<String> result = new ArrayList<>();
        Set<Long> clientIdsToDelete = new HashSet<>();
        List<ClientUser> clientUsers = new ArrayList<>();

        try (Workbook workbook = new XSSFWorkbook(is)) {

            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0 || isRowEmpty(row)) continue;

                Optional<ClientUser> optionalClientUser = parseClientUserRow(row, result, clientIdsToDelete);

                optionalClientUser.ifPresent(clientUsers::add);

            }

            if (result.isEmpty()) {
                clientIdsToDelete.forEach(clientUserRepository::deleteAllByClientId);
                clientUserRepository.saveAll(clientUsers);
                result.add("Шаблон загружен успешно. Загружено " + clientUsers.size() + " строк");
                log.info("Загружено {} строк", clientUsers.size());
            } else {
                log.warn("Ошибки при загрузке Excel: {}", result);
            }

        } catch (Exception e) {
            log.error("Ошибка при обработке Excel-файла", e);
            throw new RuntimeException("Ошибка при обработке Excel-файла", e);
        }

        return result;
    }

    private Optional<ClientUser> parseClientUserRow(Row row, List<String> result, Set<Long> clientIdsToDelete) {
        ClientUser clientUser = new ClientUser();

        String clientCode = getCellStringValue(row.getCell(0));

        if (clientCode.isEmpty()) {
            result.add("Строка " + (row.getRowNum() + 1) + ": не задан код клиента.");
            return Optional.empty();
        }

        List<VClient> vClients =
                vClientRepository.findAllByVcCode(clientCode);

        if (vClients.isEmpty()) {
            result.add("Строка " + (row.getRowNum() + 1) + ": не найден клиент с заданным кодом.");
            return Optional.empty();
        }
        VClient vClient = vClients.get(0);
        clientUser.setNClientId(vClient.getNClientId());
        clientUser.setVcLastName(getCellStringValue(row.getCell(1)));
        clientUser.setVcFirstName(getCellStringValue(row.getCell(2)));
        clientUser.setVcSecondName(getCellStringValue(row.getCell(3)));
        clientUser.setVcLogin(getCellStringValue(row.getCell(4)));
        //  Проверяем уникальность логина
        Optional<ClientUser> optionalClientUserUnique = clientUserRepository.findByVcLogin(clientUser.getVcLogin());
        if (optionalClientUserUnique.isPresent()) {
            result.add("Строка " + (row.getRowNum() + 1) + ": логин не является уникальным.");
            return Optional.empty();
        }

        //  шифруем пароль
        clientUser.setVcPassword(getCellStringValue(row.getCell(5)));
        if (clientUser.getVcPassword() != null) {
            clientUser.setVcPassword(sha512PasswordEncoder.encode(clientUser.getVcPassword()));
        }

        clientUser.setVcEmail(getCellStringValue(row.getCell(6)));
        clientUser.setVcPhone(getCellStringValue(row.getCell(7)));
        clientUser.setNRoleId(3L);

        clientIdsToDelete.add(vClient.getNClientId());
        return Optional.of(clientUser);
    }

    public byte[] processClientReserveFromExcel(InputStream inputStream) {
        try (Workbook workbook = new XSSFWorkbook(inputStream);
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            CreationHelper creationHelper = workbook.getCreationHelper();
            short timeFormat = creationHelper.createDataFormat().getFormat("HH:mm");

            CellStyle styleReserved = workbook.createCellStyle();
            styleReserved.setFillForegroundColor(IndexedColors.ROSE.getIndex());
            styleReserved.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            styleReserved.setDataFormat(timeFormat);

            CellStyle styleFree = workbook.createCellStyle();
            styleFree.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
            styleFree.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            styleFree.setDataFormat(timeFormat);

            Sheet sheet = workbook.getSheetAt(0);
            int processedRows = 0;

            for (Row row : sheet) {
                if (row.getRowNum() == 0 || isRowEmpty(row)) continue;
                parseClientReserveRow(row, styleReserved, styleFree);
                processedRows++;
            }

            log.info("Обработано {} строк в листе '{}'", processedRows, sheet.getSheetName());

            workbook.write(out);
            return out.toByteArray();

        } catch (IOException e) {
            log.error("Ошибка при чтении Excel-файла", e);
            throw new RuntimeException("Ошибка при чтении Excel-файла", e);
        } catch (Exception e) {
            log.error("Ошибка при обработке Excel-файла", e);
            throw new RuntimeException("Ошибка при обработке Excel-файла", e);
        }
    }

    private void parseClientReserveRow(Row row, CellStyle styleReserved, CellStyle styleFree) {
        List<String> result = new ArrayList<>();

        String storeCode = getCellStringValue(row.getCell(0));
        String loadingPointCode = getCellStringValue(row.getCell(1));

        if (storeCode.isEmpty()) {
            result.add("Строка " + (row.getRowNum() + 1) + ": не задан код нефтебазы.");
        }
        if (loadingPointCode.isEmpty()) {
            result.add("Строка " + (row.getRowNum() + 1) + ": не задан код пункта налива.");
        }

        Long loadingPointId = null;
        List<VLoadingPoint> vLoadingPoints = vLoadingPointRepository
                .findAllByStoreCodeAndLoadingPointCode(storeCode, loadingPointCode);
        if (vLoadingPoints.isEmpty()) {
            result.add("Строка " + (row.getRowNum() + 1) + ": не найдена запись для код нефтебазы = " +
                    storeCode + ", код пункта налива = " + loadingPointCode);
        } else {
            loadingPointId = vLoadingPoints.get(0).getNLoadingPointId();
        }

        LocalDate slotDate = getDateFromCell(row.getCell(2), "Дата слота", row.getRowNum(), result);
        LocalTime[] times = {
                getTimeFromCell(row.getCell(3), "Время начала слота 1", row.getRowNum(), result),
                getTimeFromCell(row.getCell(4), "Время начала слота 2", row.getRowNum(), result),
                getTimeFromCell(row.getCell(5), "Время начала слота 3", row.getRowNum(), result)
        };

        boolean slotFound = false;

        for (int i = 0; i < times.length; i++) {
            Cell cell = row.getCell(3 + i);
            List<Slot> slots = slotRepository.findAllFreeSlotsByPointId(loadingPointId, slotDate, times[i]);

            if (slots.isEmpty()) {
                cell.setCellStyle(styleReserved);
            } else {
                cell.setCellStyle(styleFree);
                if (!slotFound) {
                    Slot slot = slots.get(0);
                    slot.setNStatusId(RESERVED);
                    slotRepository.save(slot);
                    slotFound = true;
                }
                break;
            }
        }

        if (!slotFound) {
            result.add("Не найдены свободные слоты");
        }

        if (!result.isEmpty()) {
            Cell commentCell = row.createCell(6);
            commentCell.setCellValue(String.join("; ", result));
        }
    }

    private String getCellStringValue(Cell cell) {
        if (cell == null) return "";
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue().trim();
            case NUMERIC -> String.valueOf((int) cell.getNumericCellValue());
            default -> "";
        };
    }

    private LocalDate getDateFromCell(Cell cell, String label, int rowNum, List<String> result) {
        if (cell == null || !DateUtil.isCellDateFormatted(cell)) {
            result.add("Строка " + (rowNum + 1) + ": " + label + " - неверный формат.");
            return null;
        }
        Date date = cell.getDateCellValue();
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private LocalTime getTimeFromCell(Cell cell, String label, int rowNum, List<String> result) {
        if (cell == null || !DateUtil.isCellDateFormatted(cell)) {
            result.add("Строка " + (rowNum + 1) + ": " + label + " - неверный формат.");
            return null;
        }
        Date date = cell.getDateCellValue();
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
    }

    private boolean isRowEmpty(Row row) {
        for (int i = 0; i < 5; i++) {
            Cell cell = row.getCell(i);
            if (cell != null && cell.getCellType() != CellType.BLANK) return false;
        }
        return true;
    }

}
