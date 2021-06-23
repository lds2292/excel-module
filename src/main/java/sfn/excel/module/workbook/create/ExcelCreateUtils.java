package sfn.excel.module.workbook.create;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import sfn.excel.module.workbook.create.models.CreateWorkbookModels;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

public class ExcelCreateUtils {

    public static byte[] createExcelFile(CreateWorkbookModels.Workbook workbook) throws IOException {
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook();
        FormulaEvaluator formulaEvaluator = xssfWorkbook.getCreationHelper().createFormulaEvaluator();

        workbook.worksheets.forEach(new Consumer<CreateWorkbookModels.Worksheet>() {
            @Override
            public void accept(CreateWorkbookModels.Worksheet ws) {
                // set sheet name
                XSSFSheet xssfSheet = xssfWorkbook.createSheet(ws.worksheetName);

                // create header row
                XSSFRow xssfRow;
                int rowIdx = 0;
                xssfRow = xssfSheet.createRow(rowIdx);
                for (int i = 0; i < ws.columnHeaderDisplayNames.size(); i++) {
                    XSSFCell xssfCell = xssfRow.createCell(i);
                    xssfCell.setCellValue(ws.columnHeaderDisplayNames.get(i));
                }

                // create data rows
                for (int i = 0; i < ws.rows.size(); i++) {
                    rowIdx += 1;
                    xssfRow = xssfSheet.createRow(rowIdx);

                    // create cells for each row
                    List<Object> cells = ws.rows.get(i);
                    for (int j = 0; j < cells.size(); j++) {
                        XSSFCell xssfCell = xssfRow.createCell(j);
                        Object v = cells.get(j);
                        if (v instanceof Boolean) {
                            xssfCell.setCellValue((Boolean) v);
                            xssfCell.setCellType(CellType.BOOLEAN);
                        } else if (v instanceof Double) {
                            xssfCell.setCellValue((Double) v);
                            xssfCell.setCellType(CellType.NUMERIC);
                        } else if (v instanceof Integer) {
                            xssfCell.setCellValue((Integer) v);
                            xssfCell.setCellType(CellType.NUMERIC);
                        } else if (v instanceof Date) {
                            xssfCell.setCellValue((Date) v);
                            xssfCell.setCellType(CellType.NUMERIC);
                        } else if (v instanceof String) {
                            xssfCell.setCellValue((String) v);
                            xssfCell.setCellType(CellType.STRING);
                        } else {
                            xssfCell.setCellValue(v.toString());
                            xssfCell.setCellType(CellType.STRING);
                        }
                    }
                }

                // summary row
                rowIdx += 1;
                if (ws.summaryRowTypes != null) {
                    xssfRow = xssfSheet.createRow(rowIdx);
                    for (int i = 0; i < ws.summaryRowTypes.size(); i++) {
                        XSSFCell cell = xssfRow.createCell(i);
                        String col = CellReference.convertNumToColString(i);
                        if ("sum".equals(ws.summaryRowTypes.get(i))) {
                            cell.setCellFormula(
                                    "SUM(" + col + "2:" + col + rowIdx + ")"
                            );
                        } else if (ws.summaryRowTypes.get(i) != null) {
                            cell.setCellValue(ws.summaryRowTypes.get(i));
                        } else {
                            cell.setCellValue("");
                        }
                    }
                }
            }
        });
        formulaEvaluator.evaluateAll();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        xssfWorkbook.write(baos);
        return baos.toByteArray();
    }
}
