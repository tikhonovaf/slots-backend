package ru.ttk.slotsbe.backend.service.excel;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ru.ttk.slotsbe.backend.model.VSlot;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Slf4j
public class ExcelGenerator {

    private static final String[] HEADERS = {
            "Нефтебаза", "Пункт налива", "Дата слота", "Время начала", "Время окончания", "Статус",
             "Клиент"
    };

    public static byte[] generateExcelSlots(List<VSlot> slots) {
        log.info("Начало генерации Excel-отчета. Количество слотов: {}", slots.size());

        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Слоты");

            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle timeStyle = createTimeStyle(workbook);
            CellStyle dateStyle = createDateStyle(workbook);

            // Заголовки
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < HEADERS.length; i++) {
                createCell(headerRow, i, HEADERS[i], headerStyle);
            }

            // Данные
            int rowNum = 1;
            for (VSlot slot : slots) {
                Row row = sheet.createRow(rowNum++);
                createCell(row, 0, slot.getVcStoreCode(), null);
                createCell(row, 1, slot.getVcLoadingPointCode(), null);
                createCell(row, 2, java.sql.Date.valueOf(slot.getDDate()), dateStyle);
                createCell(row, 3, java.sql.Time.valueOf(slot.getDStartTime()), timeStyle);
                createCell(row, 4, java.sql.Time.valueOf(slot.getDEndTime()), timeStyle);
                createCell(row, 5, slot.getVcStatusName(), null);
                createCell(row, 6, slot.getVcClientName(), null);
            }

            for (int i = 0; i < HEADERS.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            log.info("Excel-отчет успешно сгенерирован.");
            return out.toByteArray();
        } catch (IOException e) {
            log.error("Ошибка при генерации Excel-отчета", e);
            throw new RuntimeException("Ошибка при генерации Excel-файла", e);
        }
    }

    private static void createCell(Row row, int columnIndex, Object value, CellStyle style) {
        Cell cell = row.createCell(columnIndex);
        if (value instanceof String) {
            cell.setCellValue((String) value);
        } else if (value instanceof java.util.Date) {
            cell.setCellValue((java.util.Date) value);
        }
        if (style != null) {
            cell.setCellStyle(style);
        }
    }

    private static CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;
    }

    private static CellStyle createTimeStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setDataFormat(workbook.getCreationHelper()
                .createDataFormat().getFormat("HH:mm"));
        return style;
    }

    private static CellStyle createDateStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setDataFormat(workbook.getCreationHelper()
                .createDataFormat().getFormat("dd.MM.yyyy"));
        return style;
    }
}
