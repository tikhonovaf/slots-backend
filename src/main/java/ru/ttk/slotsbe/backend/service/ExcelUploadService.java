package ru.ttk.slotsbe.backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.ttk.slotsbe.backend.model.LoadingPoint;
import ru.ttk.slotsbe.backend.model.SlotTemplate;
import ru.ttk.slotsbe.backend.model.VLoadingPoint;
import ru.ttk.slotsbe.backend.model.VStore;
import ru.ttk.slotsbe.backend.repository.*;

import java.io.InputStream;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExcelUploadService {

    private final SlotTemplateRepository slotTemplateRepository;
    private final VLoadingPointRepository vLoadingPointRepository;
    private final SlotStatusRepository slotStatusRepository;
    private final VStoreRepository vStoreRepository;
    private final LoadingPointRepository loadingPointRepository;

    public List<String> saveSlotTemplatesFromExcel(MultipartFile file) {
        List<String> result = new ArrayList<>();
        Set<Long> storeIdsToDelete = new HashSet<>();
        List<SlotTemplate> templates = new ArrayList<>();

        try (InputStream is = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(is)) {

            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0 || isRowEmpty(row)) continue;

                Optional<SlotTemplate> optionalTemplate = parseRow(row, result, storeIdsToDelete);
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


    private Optional<SlotTemplate> parseRow(Row row, List<String> result, Set<Long> storeIdsToDelete) {
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

    private String getCellStringValue(Cell cell) {
        if (cell == null) return "";
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue().trim();
            case NUMERIC -> String.valueOf((int) cell.getNumericCellValue());
            default -> "";
        };
    }

    private LocalTime getTimeFromCell(Cell cell, String label, int rowNum, List<String> result) {
        if (cell == null || !DateUtil.isCellDateFormatted(cell)) {
            result.add("Строка " + (rowNum + 1) + ": " + label + " - неверный формат.");
            return null;
        }
        Date date = cell.getDateCellValue();
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
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

    private boolean isRowEmpty(Row row) {
        for (int i = 0; i < 5; i++) {
            Cell cell = row.getCell(i);
            if (cell != null && cell.getCellType() != CellType.BLANK) return false;
        }
        return true;
    }

    public List<String> saveLoadingPointsFromExcel(MultipartFile file) {
        List<String> result = new ArrayList<>();
        Set<Long> storeIdsToDelete = new HashSet<>();
        List<LoadingPoint> loadingPoints = new ArrayList<>();

        try (InputStream is = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(is)) {

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


}
