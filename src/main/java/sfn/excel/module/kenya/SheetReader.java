package sfn.excel.module.kenya;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Sheet;
import sfn.excel.module.kenya.validator.RowValidator;
import sfn.excel.module.kenya.validator.ValidateResult;

public class SheetReader implements DocumentReader {

    private final Sheet sheet;
    private final DataFormatter dataFormatter = new DataFormatter();
    private final DateTimeFormatter defaultDateTimeFormatter;

    public SheetReader(Sheet sheet, DateTimeFormatter defaultDateTimeFormatter) {
        if (isEmpty(sheet)) {
            throw new IllegalArgumentException("Sheet Empty!");
        }
        this.sheet = sheet;
        this.defaultDateTimeFormatter = defaultDateTimeFormatter;
    }

    @Override
    public int lastCellCount(int row) {
        return this.sheet.getRow(row).getLastCellNum();
    }

    @Override
    public int lastRowCount() {
        return this.sheet.getLastRowNum();
    }

    public String getSheetName() {
        return this.sheet.getSheetName();
    }

    private static boolean isEmpty(Sheet sheet) {
        return sheet.getLastRowNum() == -1 && sheet.getRow(0) == null;
    }

    @Override
    public <T> List<T> classFrom(Class<T> clazz) {
        return this.classFrom(0, clazz);
    }

    @Override
    public <T> List<T> classFrom(int headerRow, Class<T> clazz) {
        List<String> columnNames = getColumnHeaders(headerRow);

        List<T> ret = new ArrayList<>();
        int rowCount = this.sheet.getLastRowNum();
        int cellCount = columnNames.size();
        for (int row = headerRow + 1; row <= rowCount; row++) {
            List<CellValue> columnList = new ArrayList<>();
            for (int col = 0; col < cellCount; col++) {
                columnList.add(this.cell(row, col));
            }
            Row customRow = new Row(columnNames, columnList);
            ret.add(ClassSupport.createInstance(customRow, clazz));
        }

        return ret;
    }

    /**
     * Functional에 구현한 함수를 실행하고 결과값을 {@code List<R>}로 반환합니다.
     * <p>
     * 엑셀의 시작은 두번째 줄부터 시작하며, 첫줄은 Header(=title)로 인식합니다.
     * <p>
     * {@code Functional<Row, R>} Row의 정보를 사용하여 Cell의 값을 가져올
     * 수 있습니다.
     *
     * @param apply 실행할 함수
     * @param <R>   반환할 타입
     * @return {@code List<R>} Funtional 함수의 반환값
     */
    @Override
    public <R> List<R> rowMap(Function<Row, R> apply) {
        return this.rowMap(0, apply);
    }

    /**
     * Functional에 구현한 함수를 실행하고 결과값을 {@code List<R>}로 반환합니다.
     * <p>
     * 엑셀의 시작은 headerRow+1부터 시작하며, headerRow는 Header(=title)로 인식합니다.
     * <p>
     * {@code Functional<Row, R>} Row의 정보를 사용하여 Cell의 값을 가져올
     * 수 있습니다.
     *
     * @param headerRow header(=title)이 있는 Row 번호 첫줄의 Row Index는 0입니다.
     * @param apply     실행할 함수
     * @param <T>       반환할 타입
     * @return {@code List<R>} Funtional 함수의 반환값
     */
    @Override
    public <T> List<T> rowMap(int headerRow, Function<Row, T> apply) {
        List<String> columnNames = getColumnHeaders(headerRow);

        List<T> ret = new ArrayList<>();
        int rowCount = this.sheet.getLastRowNum();
        int cellCount = columnNames.size();
        for (int row = headerRow + 1; row <= rowCount; row++) {
            List<CellValue> columnList = new ArrayList<>();
            for (int col = 0; col < cellCount; col++) {
                columnList.add(this.cell(row, col));
            }
            Row customRow = new Row(columnNames, columnList);
            ret.add(apply.apply(customRow));
        }

        return ret;
    }

    @Override
    public void rowForEach(Consumer<Row> consumer){
        rowForEach(0, consumer);
    }

    @Override
    public void rowForEach(int headerRow, Consumer<Row> consumer){
        List<String> columnNames = getColumnHeaders(headerRow);

        int rowCount = this.sheet.getLastRowNum();
        int cellCount = columnNames.size();
        for (int row = headerRow + 1; row <= rowCount; row++) {
            List<CellValue> columnList = new ArrayList<>();
            for (int col = 0; col < cellCount; col++) {
                columnList.add(this.cell(row, col));
            }
            Row customRow = new Row(columnNames, columnList);
            consumer.accept(customRow);
        }
    }

    @Override
    public List<String> getColumnHeaders(int headerRow) {
        List<String> columnNames = new ArrayList<>();
        org.apache.poi.ss.usermodel.Row headers = this.sheet.getRow(headerRow);
        for (int headerCol = 0; headerCol < headers.getLastCellNum(); headerCol++) {
            columnNames.add(this.value(headerRow, headerCol));
        }
        return columnNames;
    }

    public String value(Cell cell) {
        return this.value(cell.getRowIndex(), cell.getColumnIndex());
    }

    private org.apache.poi.ss.usermodel.Row rowFromSheet(int row) {
        return this.sheet.getRow(row);
    }

    @Override
    public List<CellValue> row(int row) {
        return row(0, row);
    }

    @Override
    public List<CellValue> row(int headerRow, int row) {
        int columnCount = getColumnHeaders(headerRow).size();
        List<CellValue> customRow = new ArrayList<>();
        for (int col = 0; col < columnCount; col++) {
            customRow.add(this.cell(row, col));
        }

        return customRow;
    }

    private Cell cellFromRowCol(int row, int col) {
        return this.rowFromSheet(row).getCell(col);
    }

    @Override
    public CellValue cell(int row, int col) {
        Cell cell = this.cellFromRowCol(row, col);
        return new CellValue(
            cell,
            this.value(row, col)
        );
    }

    @Override
    public String value(int row, int col) {
        Cell cell = this.cellFromRowCol(row, col);
        if (cell == null) {
            return "";
        }

        try {
            switch (cell.getCellType()) {
                case NUMERIC:
                    if (DateUtil.isCellDateFormatted(cell)) {
                        LocalDateTime localdateTime = cell.getDateCellValue()
                            .toInstant()
                            .atZone(ZoneId.systemDefault())
                            .toLocalDateTime();
                        return localdateTime.format(defaultDateTimeFormatter);
                    } else {
                        return dataFormatter.formatCellValue(cell);
                    }
                case FORMULA:
                    return formulaResult(cell);
                case BLANK:
                    return "";
                default:
                    return dataFormatter.formatCellValue(cell);
            }
        } catch (RuntimeException ex) {
            System.out.println("SheetReader Error: " + row + "/" + col);
            throw new FailedGetCellValueException(row, col, ex);
        }
    }

    private String formulaResult(Cell cell) {
        switch (cell.getCachedFormulaResultType()) {
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    LocalDateTime localdateTime = cell.getDateCellValue()
                        .toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime();
                    return localdateTime.format(defaultDateTimeFormatter);
                } else {
                    return dataFormatter.formatCellValue(cell);
                }
            case BLANK:
                return "";
            default:
                return dataFormatter.formatCellValue(cell);
        }
    }


    @Override
    public List<ValidateResult> validate(int headerRow, RowValidator validator) {
        List<String> columnNames = getColumnHeaders(headerRow);

        int rowCount = this.sheet.getLastRowNum();
        int cellCount = columnNames.size();

        List<ValidateResult> ret = new ArrayList<>();
        for (int rowIndex = headerRow + 1; rowIndex <= rowCount; rowIndex++) {
            List<CellValue> columnList = new ArrayList<>();
            for (int col = 0; col < cellCount; col++) {
                columnList.add(this.cell(rowIndex, col));
            }
            Row customRow = new Row(columnNames, columnList);
            ret.addAll(validator.validate(rowIndex, customRow));
        }

        return ret;
    }

    @Override
    public List<ValidateResult> validate(RowValidator validator) {
        return this.validate(0, validator);
    }
}

