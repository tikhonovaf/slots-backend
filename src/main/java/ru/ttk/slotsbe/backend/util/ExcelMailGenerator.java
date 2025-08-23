package ru.ttk.slotsbe.backend.util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ru.ttk.slotsbe.backend.model.VSlot;

import java.io.ByteArrayOutputStream;
import java.sql.Time;
import java.time.LocalDate;
import java.util.List;

public class ExcelMailGenerator {

    private static final String[] HEADERS = {
            "Дата слота", "Время начала", "Время окончания", "Статус",
            "Нефтебаза", "Пункт налива", "Клиент"
    };

    public static byte[] generateExcel(List<VSlot> slots) throws Exception {
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Слоты");

            // Стили
            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle timeStyle = createTimeStyle(workbook);
            CellStyle dateStyle = createDateStyle(workbook);

            // Заголовки
            createHeaderRow(sheet, headerStyle);

            // Данные
            int rowNum = 1;
            for (VSlot slot : slots) {
                Row row = sheet.createRow(rowNum++);
                createDateCell(row, 0, slot.getDDate(), dateStyle);
                createTimeCell(row, 1, slot.getDStartTime(), timeStyle);
                createTimeCell(row, 2, slot.getDEndTime(), timeStyle);
                createTextCell(row, 3, slot.getVcStatusName());
                createTextCell(row, 4, slot.getVcStoreName());
                createTextCell(row, 5, slot.getVcLoadingPointName());
                createTextCell(row, 6, slot.getVcClientName());
            }

            // Автоподбор ширины колонок
            for (int i = 0; i < HEADERS.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return out.toByteArray();
        }
    }

    private static void createHeaderRow(Sheet sheet, CellStyle style) {
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < HEADERS.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(HEADERS[i]);
            cell.setCellStyle(style);
        }
    }

    private static void createDateCell(Row row, int colIndex, LocalDate date, CellStyle style) {
        if (date != null) {
            Cell cell = row.createCell(colIndex);
            cell.setCellValue(java.sql.Date.valueOf(date));
            cell.setCellStyle(style);
        }
    }

    private static void createTimeCell(Row row, int colIndex, java.time.LocalTime time, CellStyle style) {
        if (time != null) {
            Cell cell = row.createCell(colIndex);
            cell.setCellValue(Time.valueOf(time));
            cell.setCellStyle(style);
        }
    }

    private static void createTextCell(Row row, int colIndex, String value) {
        row.createCell(colIndex).setCellValue(value != null ? value : "");
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
        style.setDataFormat(workbook.getCreationHelper().createDataFormat().getFormat("HH:mm"));
        return style;
    }

    private static CellStyle createDateStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setDataFormat(workbook.getCreationHelper().createDataFormat().getFormat("dd.MM.yyyy"));
        return style;
    }
}
