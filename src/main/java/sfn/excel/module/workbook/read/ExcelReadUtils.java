package sfn.excel.module.workbook.read;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import sfn.excel.module.workbook.read.models.SimpleWorkbookModels;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExcelReadUtils {
    public static SimpleWorkbookModels.Workbook extractSimple(
            String workbookName, InputStream inputStream
    ) throws IOException {
        SimpleWorkbookModels.Workbook responseSimpleWorkbook =
                new SimpleWorkbookModels.Workbook(workbookName, new ArrayList<>());

        XSSFWorkbook wb = new XSSFWorkbook(inputStream);
        DataFormatter dataFormatter = new DataFormatter();

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
                    String cellStringValue = dataFormatter.formatCellValue(cell);
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
