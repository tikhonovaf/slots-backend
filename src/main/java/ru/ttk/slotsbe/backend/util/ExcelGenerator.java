package ru.ttk.slotsbe.backend.util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ru.ttk.slotsbe.backend.model.Slot;
import ru.ttk.slotsbe.backend.model.VSlot;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class ExcelGenerator {

    public static byte[] generateExcel(List<VSlot> slots) throws Exception {
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("slots");

            // Стиль для заголовков
            CellStyle headerStyle = createHeaderStyle(workbook);

            // Заголовки
            Row headerRow = sheet.createRow(0);
            String[] headers = {"Дата слота", "Время начала", "Время окончания", "Статус",
               "Нефтебаза", "Пункт налива", "Кдиент" };

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Данные
            int rowNum = 1;
            // Создаем стиль для ячейки с форматом времени
            CellStyle timeStyle = workbook.createCellStyle();
            CreationHelper createHelper = workbook.getCreationHelper();
            timeStyle.setDataFormat(createHelper.createDataFormat().getFormat("HH:mm"));

            CellStyle dateStyle = workbook.createCellStyle();
            dateStyle.setDataFormat(createHelper.createDataFormat().getFormat("DD.MM,YYYY"));

            for (VSlot slot : slots) {
                Row row = sheet.createRow(rowNum++);

                Cell cell0 = row.createCell(0);
                cell0.setCellValue(slot.getDDate());
                cell0.setCellStyle(dateStyle);

                Cell cell1 = row.createCell(1);
                // Преобразуем LocalTime в java.util.Date (через java.sql.Time)
                java.sql.Time sqlTime1 = java.sql.Time.valueOf(slot.getDStartTime());
                // Устанавливаем значение и стиль
                cell1.setCellValue(sqlTime1);
                cell1.setCellStyle(timeStyle);

                Cell cell2 = row.createCell(2);
                // Преобразуем LocalTime в java.util.Date (через java.sql.Time)
                java.sql.Time sqlTime2 = java.sql.Time.valueOf(slot.getDEndTime());
                // Устанавливаем значение и стиль
                cell2.setCellValue(sqlTime2);
                cell2.setCellStyle(timeStyle);

                row.createCell(3).setCellValue(slot.getVcStatus());
                row.createCell(4).setCellValue(slot.getVcStoreName());
                row.createCell(5).setCellValue(slot.getVcLoadingPointName());
                row.createCell(6).setCellValue(slot.getVcClientName());
            }

            // Автоподбор ширины колонок
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return out.toByteArray();
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
}