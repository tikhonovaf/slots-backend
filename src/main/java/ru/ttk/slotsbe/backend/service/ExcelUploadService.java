package ru.ttk.slotsbe.backend.service;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.ttk.slotsbe.backend.model.SlotTemplate;
import ru.ttk.slotsbe.backend.model.VLoadingPoint;
import ru.ttk.slotsbe.backend.repository.SlotTemplateRepository;
import ru.ttk.slotsbe.backend.repository.VLoadingPointRepository;

import java.io.InputStream;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor

public class ExcelUploadService {
    @Autowired
    private SlotTemplateRepository slotTemplateRepository;
    @Autowired
    private VLoadingPointRepository vLoadingPointRepository;

//    @Autowired
//    SlotTemplateService slotTemplateService;

    public List<String> saveSlotTemplatesFromExcel(MultipartFile file) {
        List<String> result = new ArrayList<>();
        Long storeId = 0L;
        try (InputStream is = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(is)) {

            Sheet sheet = workbook.getSheetAt(0);
            List<SlotTemplate> templates = new ArrayList<>();

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Пропустить заголовок

                SlotTemplate template = new SlotTemplate();

                // Определение пункта налива
                Cell cellStoreCode = row.getCell(0);
                String storeCode;
                if (cellStoreCode.getCellType() == CellType.NUMERIC) {
                    // Если ячейка содержит число, преобразуем в строку
                    storeCode = String.valueOf((int) cellStoreCode.getNumericCellValue());
                } else {
                    // Если ячейка уже содержит текст
                    storeCode = cellStoreCode.getStringCellValue();
                }
                if (storeCode.isEmpty()) {
                    result.add("Строка " + (row.getRowNum() + 1) + ": не задан код нефтебазы.");
                }

                Cell cellLoadingPointCode = row.getCell(1);
                String loadingPointCode;
                if (cellLoadingPointCode.getCellType() == CellType.NUMERIC) {
                    // Если ячейка содержит число, преобразуем в строку
                    loadingPointCode = String.valueOf((int) cellLoadingPointCode.getNumericCellValue());
                } else {
                    // Если ячейка уже содержит текст
                    loadingPointCode = cellLoadingPointCode.getStringCellValue();
                }
                if (loadingPointCode.isEmpty()) {
                    result.add("Строка " + (row.getRowNum() + 1) + ": не задан код пункта налива.");
                }
                List<VLoadingPoint> vLoadingPoints =
                        vLoadingPointRepository.findAllByStoreCodeAndLoadingPointCode(storeCode, loadingPointCode);
                if (vLoadingPoints.size() > 0) {
                    template.setNLoadingPointId(vLoadingPoints.get(0).getNLoadingPointId());

                    //  Удаляем все записи в шаблоне для данной нефтебазы
                    if (!vLoadingPoints.get(0).getNStoreId().equals(storeId)) {
                        storeId = vLoadingPoints.get(0).getNStoreId();
                        slotTemplateRepository.deleteAllByStoreId(storeId);
                    }

                } else {
                    result.add("Строка " + (row.getRowNum() + 1) + ": не удалось найти запись в БД " +
                            "для код нефтебазы = " + storeCode + ", код пункта налива = " + loadingPointCode);
                }

                //  Время начала слота
                Cell cellStartTime = row.getCell(2);
                if (DateUtil.isCellDateFormatted(cellStartTime)) {
                    Date date = cellStartTime.getDateCellValue(); // получаем java.util.Date
                    LocalTime startTime = date.toInstant()
                            .atZone(ZoneId.systemDefault())
                            .toLocalTime();
                    template.setDStartTime(startTime);
                } else {
                    result.add("Строка " + (row.getRowNum() + 1) + ": Время начала слота - неверный формат.");
                }
                //  Время окончания слота
                Cell cellEndTime = row.getCell(3);
                if (DateUtil.isCellDateFormatted(cellStartTime)) {
                    Date date = cellStartTime.getDateCellValue(); // получаем java.util.Date
                    LocalTime endTime = date.toInstant()
                            .atZone(ZoneId.systemDefault())
                            .toLocalTime();
                    template.setDEndTime(endTime);
                } else {
                    result.add("Строка " + (row.getRowNum() + 1) + ": Время окончания слота - неверный формат.");
                }

                templates.add(template);
            }

            if (result.size() == 0) {
                result.add("Шаблон загружен успешно ");
                slotTemplateRepository.saveAll(templates);
            }

        } catch (Exception e) {
            throw new RuntimeException("Ошибка при обработке Excel-файла", e);
        }
        return result;
    }
}
