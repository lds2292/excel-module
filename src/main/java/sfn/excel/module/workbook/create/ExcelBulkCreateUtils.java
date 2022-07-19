package sfn.excel.module.workbook.create;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import sfn.excel.module.workbook.create.models.CreateWorkbookModels;

public class ExcelBulkCreateUtils {

    public static String createExcelBulkFile(CreateWorkbookModels.Workbook workbook)
        throws IOException {
        // setRandomAccessWindowSize default 100
        SXSSFWorkbook sxssfWorkbook = new SXSSFWorkbook();

        workbook.worksheets.forEach(new Consumer<CreateWorkbookModels.Worksheet>() {
            @Override
            public void accept(CreateWorkbookModels.Worksheet ws) {
                // set sheet name
                SXSSFSheet sxssfSheet = sxssfWorkbook.createSheet(ws.worksheetName);

                // create header row
                SXSSFRow sxssfRow;
                int rowIdx = 0;
                sxssfRow = sxssfSheet.createRow(rowIdx);
                for (int i = 0; i < ws.columnHeaderDisplayNames.size(); i++) {
                    SXSSFCell sxssfCell = sxssfRow.createCell(i);
                    sxssfCell.setCellValue(ws.columnHeaderDisplayNames.get(i));
                }

                // create data rows
                for (int i = 0; i < ws.rows.size(); i++) {
                    rowIdx += 1;
                    sxssfRow = sxssfSheet.createRow(rowIdx);

                    // create cells for each row
                    List<Object> cells = ws.rows.get(i);
                    for (int j = 0; j < cells.size(); j++) {
                        SXSSFCell sxssfCell = sxssfRow.createCell(j);
                        Object v = cells.get(j);
                        if (v == null) {
                            continue;
                        }

                        if (v instanceof Boolean) {
                            sxssfCell.setCellValue((Boolean) v);
                            sxssfCell.setCellType(CellType.BOOLEAN);
                        } else if (v instanceof Double) {
                            sxssfCell.setCellValue((Double) v);
                            sxssfCell.setCellType(CellType.NUMERIC);
                        } else if (v instanceof Integer) {
                            sxssfCell.setCellValue((Integer) v);
                            sxssfCell.setCellType(CellType.NUMERIC);
                        } else if (v instanceof Date) {
                            sxssfCell.setCellValue((Date) v);
                            sxssfCell.setCellType(CellType.NUMERIC);
                        } else if (v instanceof String) {
                            sxssfCell.setCellValue((String) v);
                            sxssfCell.setCellType(CellType.STRING);
                        } else {
                            sxssfCell.setCellValue(v.toString());
                            sxssfCell.setCellType(CellType.STRING);
                        }
                    }

                    try {
                        sxssfSheet.flushRows(100);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

                // summary row
                rowIdx += 1;
                if (ws.summaryRowTypes != null) {
                    sxssfRow = sxssfSheet.createRow(rowIdx);
                    for (int i = 0; i < ws.summaryRowTypes.size(); i++) {
                        SXSSFCell cell = sxssfRow.createCell(i);
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


        String fileFullPath = workbook.filePath + "/" + workbook.fileName;

        FileOutputStream fos = new FileOutputStream(fileFullPath);
        sxssfWorkbook.write(fos);
        fos.close();
        sxssfWorkbook.dispose();

        return fileFullPath;
    }
}
