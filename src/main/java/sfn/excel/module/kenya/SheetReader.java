package sfn.excel.module.kenya;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public class SheetReader {

    private final Sheet sheet;
    private final DataFormatter dataFormatter = new DataFormatter();
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(
            "yyyy-MM-dd HH:mm:ss");

    public SheetReader(Sheet sheet) {
        if (isEmpty(sheet)) {
            throw new IllegalArgumentException("Sheet Empty!");
        }
        this.sheet = sheet;
    }

    public int lastCellNum(int rownum) {
        return this.sheet.getRow(rownum).getLastCellNum();
    }
    public int lastRowNum() {
        return this.sheet.getLastRowNum();
    }

    public String getSheetName() {
        return this.sheet.getSheetName();
    }

    private static boolean isEmpty(Sheet sheet) {
        return sheet.getLastRowNum() == 0 && sheet.getRow(0) == null;
    }

    public <T> List<T> run(Class<T> clazz) {
        return this.run(0, clazz);
    }

    public <T> List<T> run(int headerRow, Class<T> clazz) {
        List<String> columnNames = readColumnHeaders(headerRow);

        List<T> ret = new ArrayList<>();
        int rowCount = this.sheet.getLastRowNum();
        int cellCount = this.sheet.getRow(headerRow).getLastCellNum();
        for (int row = headerRow + 1; row <= rowCount; row++) {
            List<CellValue> columnList = new ArrayList<>();
            for (int col = 0; col < cellCount; col++){
                columnList.add(new CellValue(this.value(row, col)));
            }
            Cells cells = new Cells(columnNames, columnList);
            ret.add(ClassSupport.createInstance(cells, clazz));
        }

        return ret;
    }

    /**
     * Functional에 구현한 함수를 실행하고 결과값을 {@code List<R>}로 반환합니다.
     * <p>
     * 엑셀의 시작은 두번째 줄부터 시작하며, 첫줄은 Header(=title)로 인식합니다.
     * <p>
     * {@code Functional<Cells, R>}중 Cells는 Row에 있는 모든 Cell의 정보가 있습니다. 이 Cell의 정보를 사용하여 Cell의 값을 가져올 수 있습니다.
     * @param apply 실행할 함수
     * @param <R> 반환할 타입
     * @return {@code List<R>} Funtional 함수의 반환값
     */
    public <R> List<R> run(Function<Cells, R> apply) {
        return this.run(0, apply);
    }

    /**
     * Functional에 구현한 함수를 실행하고 결과값을 {@code List<R>}로 반환합니다.
     * <p>
     * 엑셀의 시작은 headerRow+1부터 시작하며, headerRow는 Header(=title)로 인식합니다.
     * <p>
     * {@code Functional<Cells, R>}중 Cells는 Row에 있는 모든 Cell의 정보가 있습니다. 이 Cell의 정보를 사용하여 Cell의 값을 가져올 수 있습니다.
     * @param headerRow header(=title)이 있는 Row 번호 첫줄의 Row Index는 0입니다.
     * @param apply 실행할 함수
     * @param <T> 반환할 타입
     * @return {@code List<R>} Funtional 함수의 반환값
     */
    public <T> List<T> run(int headerRow, Function<Cells, T> apply) {
        List<String> columnNames = readColumnHeaders(headerRow);

        List<T> ret = new ArrayList<>();
        int rowCount = this.sheet.getLastRowNum();
        int cellCount = this.sheet.getRow(headerRow).getLastCellNum();
        for (int row = headerRow + 1; row <= rowCount; row++) {
            List<CellValue> columnList = new ArrayList<>();
            for (int col = 0; col < cellCount; col++){
                columnList.add(new CellValue(this.value(row, col)));
            }
            Cells cells = new Cells(columnNames, columnList);
            ret.add(apply.apply(cells));
        }

        return ret;
    }

    private List<String> readColumnHeaders(int headerRow){
        List<String> columnNames = new ArrayList<>();
        Row headers = this.sheet.getRow(headerRow);
        for (int headerCol = 0; headerCol < headers.getLastCellNum(); headerCol++) {
            columnNames.add(this.value(headerRow, headerCol));
        }
        return columnNames;
    }

    public String value(Cell cell) {
        return this.value(cell.getRowIndex(), cell.getColumnIndex());
    }

    private Row row(int rownum) {
        return this.sheet.getRow(rownum);
    }

    private Cell cell(int row, int colnum) {
        return this.row(row).getCell(colnum);
    }

    public String value(int row, int col) {
        Cell cell = this.cell(row, col);
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
                        return localdateTime.format(dateTimeFormatter);
                    } else {
                        return String.format("%f", cell.getNumericCellValue());
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
                    return localdateTime.format(dateTimeFormatter);
                } else {
                    return String.format("%f", cell.getNumericCellValue());
                }
            case BLANK:
                return "";
            default:
                return dataFormatter.formatCellValue(cell);
        }
    }

}

