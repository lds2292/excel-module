package sfn.excel.module.kenya;

import org.apache.poi.ss.usermodel.*;

public class DefaultHeaderStyle implements HeaderStyle {
    @Override
    public CellStyle defineCellStyle(Workbook wb) {
        CellStyle cs = wb.createCellStyle();
        alignCenter(cs);
        fillGreyColor(cs);
        aroundBorderStyle(cs);
        return cs;
    }

    private void fillGreyColor(CellStyle cs) {
        cs.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cs.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
    }

    private void alignCenter(CellStyle cs) {
        cs.setVerticalAlignment(VerticalAlignment.CENTER);
        cs.setAlignment(HorizontalAlignment.CENTER);
    }

    private void aroundBorderStyle(CellStyle cs) {
        cs.setBorderTop(BorderStyle.THIN);
        cs.setTopBorderColor(IndexedColors.BLACK.getIndex());
        cs.setBorderRight(BorderStyle.THIN);
        cs.setRightBorderColor(IndexedColors.BLACK.getIndex());
        cs.setBorderBottom(BorderStyle.THIN);
        cs.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        cs.setBorderLeft(BorderStyle.THIN);
        cs.setLeftBorderColor(IndexedColors.BLACK.getIndex());
    }
}
