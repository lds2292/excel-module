package sfn.excel.module.kenya;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;

public interface HeaderStyle {
    CellStyle defineCellStyle(Workbook wb);
}
