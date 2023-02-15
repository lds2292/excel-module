package sfn.excel.module.kenya;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.function.Consumer;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
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

    public String getSheetName() {
        return this.sheet.getSheetName();
    }

    private static boolean isEmpty(Sheet sheet) {
        return sheet.getLastRowNum() == 0 && sheet.getRow(0) == null;
    }

    public void repeatAllCell(int headerRowIndex, Consumer<String> action) {
        int rowCount = this.sheet.getLastRowNum();
        int cellCount = this.sheet.getRow(headerRowIndex).getLastCellNum();
        for (int row = headerRowIndex + 1; row <= rowCount; row++) {
            for (int col = 0; col < cellCount; col++) {
                action.accept(this.value(row, col));
            }
        }
    }

    private String value(Cell cell) {
        return this.value(cell.getRowIndex(), cell.getColumnIndex());
    }

    public String value(int row, int col) {
        Cell cell = this.sheet.getRow(row).getCell(col);
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
            System.out.println("SheetReader Error: "+row+"/"+col);
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

