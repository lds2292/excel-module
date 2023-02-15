package sfn.excel.module.kenya;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Sheet;

public class SheetReader {
    private static final int START_INDEX = 0;

    private final Sheet sheet;
    private final int startIndexAt;
    private final DataFormatter dataFormatter = new DataFormatter();
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public SheetReader(Sheet sheet) {
        if (isEmpty(sheet)) throw new IllegalArgumentException("Sheet Empty!");
        this.sheet = sheet;
        this.startIndexAt = START_INDEX;
    }

    public SheetReader(Sheet sheet, int startIndexAt) {
        if (isEmpty(sheet)) throw new IllegalArgumentException("Sheet Empty!");
        this.sheet = sheet;
        this.startIndexAt = startIndexAt;
    }

    public int lastCellNum(int rownum) {
        return this.sheet.getRow(rownum).getLastCellNum();
    }

    private static boolean isEmpty(Sheet sheet){
        return sheet.getLastRowNum() == 0 && sheet.getRow(0) == null;
    }

    public String value(int row, int col) {
        Cell cell = this.sheet.getRow(row).getCell(col);
        if (cell == null) return "";
        System.out.println(cell.getCellType());
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
    }

    private String formulaResult(Cell cell) {
        switch(cell.getCachedFormulaResultType()){
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

