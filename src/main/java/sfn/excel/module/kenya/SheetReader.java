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

    public <R> List<R> run(Function<Cells, R> apply) {
        return this.run(0, apply);
    }

    public <R> List<R> run(int headerRow, Function<Cells, R> apply) {
        int rowCount = this.sheet.getLastRowNum();
        int cellCount = this.sheet.getRow(headerRow).getLastCellNum();
        List<R> ret = new ArrayList<>();

        List<String> columnNames = new ArrayList<>();
        Row headers = this.sheet.getRow(headerRow);
        for (int headerCol = 0; headerCol < headers.getLastCellNum(); headerCol++) {
            columnNames.add(this.value(headerRow, headerCol));
        }

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
            throw ex;
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

