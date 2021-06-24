package sfn.excel.module.workbook.read;

import com.google.gson.Gson;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import sfn.excel.module.workbook.read.models.SimpleWorkbookModels;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

        XSSFWorkbook wb = new XSSFWorkbook(inputStream);
        DataFormatter dataFormatter = new DataFormatter();
//        XSSFDataFormat dataFormat = wb.getCreationHelper().form;

        int sheetIdx = 0;
        Iterator<Sheet> sheetIterator = wb.sheetIterator();
        while (sheetIterator.hasNext()) {
            // data - sheet
            Sheet sheet = sheetIterator.next();

            // response - sheet
            SimpleWorkbookModels.Worksheet responseSimpleWorksheet = new SimpleWorkbookModels.Worksheet(
                    sheet.getSheetName(), sheetIdx, new ArrayList<>()
            );
            responseSimpleWorkbook.worksheets.add(responseSimpleWorksheet);

            // rows
            Iterator<Row> rowIterator = sheet.rowIterator();
            List<List<String>> responseRows = new ArrayList<>();
            while (rowIterator.hasNext()) {
                List<String> responseRow = new ArrayList<>();

                Row row = rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    String cellStringValue = "";
                    try {
                        switch (cell.getCellType()) {
                            case NUMERIC:
                                // cellStringValue = String.valueOf(cell.getNumericCellValue());
                                cellStringValue = dataFormatter.formatCellValue(cell);
                                break;
                            case STRING:
                                // cellStringValue = cell.getStringCellValue();
                                cellStringValue = dataFormatter.formatCellValue(cell);
                                break;
                            case ERROR:
                                cell.setCellType(CellType.STRING);
                                cellStringValue = cell.getStringCellValue();
                                break;
                            case BLANK:
                                cellStringValue = "";
                                break;
                            case FORMULA:
                                cellStringValue = cell.getCellFormula();
                            case BOOLEAN:
                                cellStringValue = String.valueOf(cell.getBooleanCellValue());
                            default:
                                cellStringValue = dataFormatter.formatCellValue(cell);
                        }
                    } catch (Exception e) {
                        // e.printStackTrace();
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
