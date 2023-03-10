package sfn.excel.module.workbook.read;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import sfn.excel.module.workbook.common.SfnEnums;
import sfn.excel.module.workbook.read.models.SimpleWorkbookModels;

public class ExcelReadUtils {
    private static String returnStringValue(Cell cell) {
        CellType cellType = cell.getCellType();

        switch (cellType) {
            case NUMERIC:
                double doubleVal = cell.getNumericCellValue();
                return String.valueOf(doubleVal);
            case STRING:
                return cell.getStringCellValue();
            case ERROR:
                return String.valueOf(cell.getErrorCellValue());
            case BLANK:
                return "";
            case FORMULA:
                return cell.getCellFormula();
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
        }
        return "error decoding string value of the cell";
    }

    public static SimpleWorkbookModels.Workbook extractSimple(
            String workbookName, InputStream inputStream
    ) throws Exception {
        SimpleWorkbookModels.Workbook responseSimpleWorkbook =
                new SimpleWorkbookModels.Workbook(workbookName, new ArrayList<>());

        Workbook wb = WorkbookFactory.create(inputStream);

        DataFormatter dataFormatter = new DataFormatter();

        int sheetIdx = 0;
        Iterator<Sheet> sheetIterator = wb.sheetIterator();
        while (sheetIterator.hasNext()) {
            // data - sheet
            Sheet sheet = sheetIterator.next();

            // response - sheet
            SimpleWorkbookModels.Worksheet responseSimpleWorksheet = new SimpleWorkbookModels.Worksheet(
                    sheet.getSheetName(), sheetIdx, new ArrayList<>(), new ArrayList<>()
            );
            responseSimpleWorkbook.worksheets.add(responseSimpleWorksheet);

            // rows
            Iterator<Row> rowIterator = sheet.rowIterator();
            List<List<String>> responseRows = new ArrayList<>();
            while (rowIterator.hasNext()) {
                List<String> responseRow = new ArrayList<>();

                Row row = rowIterator.next();

                for (int i = 0; i < row.getLastCellNum(); i++) {
                    Cell cell = row.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    String cellStringValue = "";
                    SfnEnums.CellType cellType;
                    try {
                        switch (cell.getCellType()) {
                            case NUMERIC:
                                // cellStringValue = String.valueOf(cell.getNumericCellValue());
                                cellStringValue = dataFormatter.formatCellValue(cell);
                                cellType = SfnEnums.CellType.NUMERIC;
                                break;
                            case STRING:
                                cellStringValue = dataFormatter.formatCellValue(cell);
                                cellType = SfnEnums.CellType.STRING;
                                break;
                            case ERROR:
                                cell.setCellType(CellType.STRING);
                                cellStringValue = cell.getStringCellValue();
                                cellType = SfnEnums.CellType.ERROR;
                                break;
                            case BLANK:
                                cellStringValue = "";
                                cellType = SfnEnums.CellType.BLANK;
                                break;
                            case FORMULA:
                                cellStringValue = cell.getCellFormula();
                                cellType = SfnEnums.CellType.FORMULA;
                            case BOOLEAN:
                                cellStringValue = String.valueOf(cell.getBooleanCellValue());
                                cellType = SfnEnums.CellType.BOOLEAN;
                            default:
                                cellStringValue = dataFormatter.formatCellValue(cell);
                                cellType = SfnEnums.CellType.DEFAULT;
                        }
                    } catch (Exception e) {
                        // e.printStackTrace();
                        cellType = SfnEnums.CellType.CUSTOM_PARSE_ERROR;

                        try {
                            cell.setCellType(CellType.STRING);
                            cellStringValue = String.valueOf(cell.getStringCellValue());
                            System.out.println("sfn-excel-module: PARSE ERROR: " +
                                    "col: " + cell.getColumnIndex() + ", " +
                                    "row: " + cell.getRowIndex() + ", " +
                                    "string cell value: " + cell.getStringCellValue()
                            );
                        } catch (Exception e2) {
                            // e2.printStackTrace();
                        }
                    }

                    if (cell.getRowIndex() == 1) responseSimpleWorksheet.columnDataTypes.add(cellType);
                    responseRow.add(cellStringValue);
                }

                responseSimpleWorksheet.rows = responseRows;
                responseRows.add(responseRow);
            }
            sheetIdx += 1;
        }

        return responseSimpleWorkbook;
    }
}
