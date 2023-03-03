package sfn.excel.module.kenya;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import sfn.excel.module.kenya.annotation.ExcelExport;
import sfn.excel.module.kenya.exception.ExcelWriteException;
import sfn.excel.module.kenya.support.DateTypeNormalizer;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.BiConsumer;

public class ExcelWriter {

    private static final int DEFAULT_START_ROW = 0;
    private static final String DEFAULT_TEMP_DIR = "/tmp";

    private final String sheetName;

    private Map<String, CellStyle> dateFormatStyles;
    private WriteConfig config = WriteConfig.Default();

    public ExcelWriter(String sheetName) {
        this.sheetName = sheetName;
    }

    public ExcelWriter(String sheetName, WriteConfig config) {
        this.sheetName = sheetName;
        this.config = config;
    }

    public String getSheetName() {
        return sheetName;
    }

    public WriteConfig getConfig() {
        return config;
    }

    /**
     * 엑셀생성을 하기 위한 메서드입니다. 첫행부터 차례대로 입력해 나갑니다.
     *
     * @param sources 소스
     * @param <T> 소스의 타입
     * @return 생성된 파일의 경로 문자열
     * @throws ExcelWriteException
     */
    public <T extends ExcelSupport> String write(List<T> sources) {
        return this.write(DEFAULT_START_ROW, sources);
    }


    /**
     * 엑셀생성을 하기 위한 메서드입니다. startRow는 데이터가 입력되는 시작지점이며 Header가 작성되는 Row Index입니다.
     *
     * @param startRow 입력할 Row의 시작 Index
     * @param sources 소스
     * @param <T> 소스의 타입
     * @return 생성된 파일의 경로 문자열
     * @throws ExcelWriteException
     */
    public <T extends ExcelSupport> String write(int startRow, List<T> sources) {

        if (sources.isEmpty()) throw new ExcelWriteException("sources must not be empty");

        try (SXSSFWorkbook wb = new SXSSFWorkbook(SXSSFWorkbook.DEFAULT_WINDOW_SIZE)) {
            wb.setCompressTempFiles(true);
            SXSSFSheet sheet = wb.createSheet(sheetName);

            CellStyle headerCellStyle = config.getHeaderStyle().defineCellStyle(wb);
            initCellDateFormat(wb);

            createHeader(startRow, headerCellStyle, sources.get(0), sheet);
            createRow(startRow, sources, sheet);

            String path = getTempDir();

            try (FileOutputStream out = new FileOutputStream(path)) {
                wb.write(out);
                out.close();

                wb.dispose();

                return path;
            }
        } catch (IOException | IllegalAccessException ex) {
            throw new ExcelWriteException(ex);
        }
    }


    /**
     * 실제로 workbook을 사용해서 Apache Poi의 엑셀 생성 함수를 구현합니다.
     *
     * @param consumer 생성 함수
     * @return 생성된 파일의 경로 문자열
     */
    public String write(BiConsumer<Workbook, Sheet> consumer) {
        try (SXSSFWorkbook wb = new SXSSFWorkbook(SXSSFWorkbook.DEFAULT_WINDOW_SIZE)) {
            consumer.accept(wb, wb.createSheet(this.sheetName));
            String path = getTempDir();

            try (FileOutputStream out = new FileOutputStream(path)) {
                wb.write(out);
                out.close();

                wb.dispose();

                return path;
            }
            
        } catch (IOException ex) {
            throw new ExcelWriteException(ex);
        }
    }

    private static String getTempDir() {
        String fileName = UUID.randomUUID().toString();
        return DEFAULT_TEMP_DIR + "/" + fileName + ".xlsx";
    }


    private void initCellDateFormat(SXSSFWorkbook wb) {
        CellStyle dateCellStyle = wb.createCellStyle();
        dateCellStyle.setDataFormat(wb.getCreationHelper().createDataFormat().getFormat(DateTypeNormalizer.DATE_FORMAT));
        CellStyle dateTimeCellStyle = wb.createCellStyle();
        dateTimeCellStyle.setDataFormat(wb.getCreationHelper().createDataFormat().getFormat(DateTypeNormalizer.DATETIME_FORMAT));

        this.dateFormatStyles = Map.of("DATE", dateCellStyle, "DATETIME", dateTimeCellStyle);
    }

    private <T extends ExcelSupport> void createRow(int startRow, List<T> sources, SXSSFSheet sheet)
            throws IllegalAccessException {
        int inputStartRow = startRow + 1;
        for (T source : sources) {
            Field[] fields = source.getClass().getDeclaredFields();
            SXSSFRow row = sheet.createRow(inputStartRow++);
            setCellValue(source, fields, row);
        }
    }

    private <T extends ExcelSupport> void setCellValue(T source, Field[] fields, SXSSFRow row)
            throws IllegalAccessException {
        for (Field field : fields) {
            if (!field.isAnnotationPresent(ExcelExport.class)) continue;
            ExcelExport ann = field.getAnnotation(ExcelExport.class);
            try {
                field.setAccessible(true);
                Class<?> type = field.getType();
                Object fieldValue = field.get(source);
                if (type.equals(Integer.class) || type.equals(int.class)) {
                    if (Objects.isNull(fieldValue) && ann.defaultValue().isBlank()) continue;
                    SXSSFCell cell = row.createCell(ann.order(), CellType.NUMERIC);
                    int value = Objects.isNull(field.get(source)) ? Integer.parseInt(ann.defaultValue()) : (int) field.get(source);
                    cell.setCellValue(value);
                } else if (type.equals(Long.class) || type.equals(long.class)) {
                    if (Objects.isNull(fieldValue) && ann.defaultValue().isBlank()) continue;
                    SXSSFCell cell = row.createCell(ann.order(), CellType.NUMERIC);
                    long value = Objects.isNull(field.get(source)) ? Long.parseLong(ann.defaultValue()) : (long) field.get(source);
                    cell.setCellValue(value);
                } else if (type.equals(Double.class) || type.equals(double.class)) {
                    if (Objects.isNull(fieldValue) && ann.defaultValue().isBlank()) continue;
                    SXSSFCell cell = row.createCell(ann.order(), CellType.NUMERIC);
                    double value = Objects.isNull(field.get(source)) ? Double.parseDouble(ann.defaultValue()) : (double) field.get(source);
                    cell.setCellValue(value);
                } else if (type.equals(LocalDate.class)) {
                    if (!Objects.isNull(field.get(source))) {
                        SXSSFCell cell = row.createCell(ann.order(), CellType.NUMERIC);
                        cell.setCellValue(DateUtil.getExcelDate((LocalDate) field.get(source)));
                        cell.setCellStyle(this.dateFormatStyles.get("DATE"));
                    }
                } else if (type.equals(LocalDateTime.class)) {
                    if (!Objects.isNull(field.get(source))) {
                        SXSSFCell cell = row.createCell(ann.order(), CellType.NUMERIC);
                        cell.setCellValue(DateUtil.getExcelDate((LocalDateTime) field.get(source)));
                        cell.setCellStyle(this.dateFormatStyles.get("DATETIME"));
                    }
                } else {
                    SXSSFCell cell = row.createCell(ann.order(), CellType.STRING);
                    String value = (String) field.get(source);
                    cell.setCellValue(Objects.isNull(value) ? ann.defaultValue() : (String) field.get(source));
                }

            } finally {
                field.setAccessible(false);
            }
        }
    }

    private <T extends ExcelSupport> void createHeader(int startRow, CellStyle headerCellStyle, T source, SXSSFSheet sheet) {

        Field[] fields = source.getClass().getDeclaredFields();

        SXSSFRow row = sheet.createRow(startRow);
        row.setHeight(config.getHeaderRowHeight());
        for (Field field : fields) {
            if (!field.isAnnotationPresent(ExcelExport.class)) continue;
            ExcelExport ann = field.getAnnotation(ExcelExport.class);
            SXSSFCell cell = row.createCell(ann.order());
            cell.setCellValue(ann.name());
            cell.setCellStyle(headerCellStyle);
        }
    }
}
