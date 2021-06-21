package sfn.excel.module;

import sfn.excel.module.models.Workbook;
import sfn.excel.module.models.Worksheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExcelUtils {

    public static String test() {
        return "test method: sfn.excell.module.ExcelUtils.test();";
    }


    public static Workbook extractSimple(String workbookName, InputStream inputStream) throws IOException {
        Workbook responseWorkbook = new Workbook(workbookName, new ArrayList<>());

        XSSFWorkbook wb = new XSSFWorkbook(inputStream);
        DataFormatter dataFormatter = new DataFormatter();

        int sheetIdx = 0;
        Iterator<Sheet> sheetIterator = wb.sheetIterator();
        while (sheetIterator.hasNext()) {
            // data - sheet
            Sheet sheet = sheetIterator.next();

            // response - sheet
            Worksheet responseWorksheet = new Worksheet(sheet.getSheetName(), sheetIdx, new ArrayList<>());
            responseWorkbook.worksheets.add(responseWorksheet);

            // rows
            Iterator<Row> rowIterator = sheet.rowIterator();
            List<List<String>> responseRows = new ArrayList<>();
            while (rowIterator.hasNext()) {
                List<String> responseRow = new ArrayList<>();

                Row row = rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    String cellStringValue = dataFormatter.formatCellValue(cell);
                    responseRow.add(cellStringValue);
                }

                responseWorksheet.rows = responseRows;

                responseRows.add(responseRow);
            }
            sheetIdx += 1;
        }

        return responseWorkbook;
    }
}
