package sfn.excel.module.kenya;

import java.util.List;

public interface DocumentReader extends ReaderSupport, DocumentValidate {
    int lastCellCount(int row);

    int lastRowCount();

    List<CellValue> row(int row);

    List<CellValue> row(int headerRow, int row);

    CellValue cell(int row, int col);

    String value(int row, int col);

    List<String> getColumnHeaders(int headerRow);
}
