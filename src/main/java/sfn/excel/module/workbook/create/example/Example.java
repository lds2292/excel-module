package sfn.excel.module.workbook.create.example;

import sfn.excel.module.workbook.create.ExcelCreateUtils;
import sfn.excel.module.workbook.create.models.CreateWorkbookModels;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Example {
    public static CreateWorkbookModels.Worksheet generateWorksheet(String prefix) {
        // create worksheet
        CreateWorkbookModels.Worksheet ws = new CreateWorkbookModels.Worksheet();
        ws.worksheetName = prefix + "-wonder-ws1";
        // column headers
        ws.columnHeaderDisplayNames = new ArrayList<>();
        ws.columnHeaderDisplayNames.add(prefix + "-wonder-ws1-h1");
        ws.columnHeaderDisplayNames.add(prefix + "-wonder-ws1-h2");
        ws.columnHeaderDisplayNames.add(prefix + "-wonder-ws1-h3");
        // data rows
        ws.rows = new ArrayList<>();
        ws.rows.add(Stream.of(prefix + "-value1", 1, 1.1).collect(Collectors.toList()));
        ws.rows.add(Stream.of(prefix + "-value2", 2, 2.2).collect(Collectors.toList()));
        ws.rows.add(Stream.of(prefix + "-value3", 3, 3.3).collect(Collectors.toList()));
        // summary row types
        ws.summaryRowTypes = new ArrayList<>();
        ws.summaryRowTypes.add("합계");
        ws.summaryRowTypes.add("sum");
        ws.summaryRowTypes.add("sum");

        return ws;
    }

    public static void creatExcelFile() {
        try {
            // create workbook
            CreateWorkbookModels.Workbook wb = new CreateWorkbookModels.Workbook();
            wb.worksheets = new ArrayList<>();

            // add worksheets
            wb.worksheets.add(generateWorksheet("1"));
            wb.worksheets.add(generateWorksheet("2"));
            wb.worksheets.add(generateWorksheet("3"));

            byte[] ba = ExcelCreateUtils.createExcelFile(wb);
            File file = new File("generated_excel_file" + System.currentTimeMillis() + ".xlsx");
            if (file.exists()) file.delete();
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(ba);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
