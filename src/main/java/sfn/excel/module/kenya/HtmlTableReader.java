package sfn.excel.module.kenya;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import org.jsoup.nodes.Element;

public class HtmlTableReader implements DocumentReader {

    private static final String TAG_TABLE_BODY = "tbody";
    private static final String TAG_TR = "tr";
    private static final String TAG_TD = "td";

    private Element body;


    public HtmlTableReader(Element body) {
        this.body = body;
    }

    @Override
    public int lastCellCount(int row) {
        Element findRow = body.getElementsByTag(TAG_TR).get(row);
        return findRow != null ? findRow.childrenSize() : 0;
    }

    @Override
    public int lastRowCount() {
        Element findTableBody = body.getElementsByTag(TAG_TABLE_BODY).first();
        return findTableBody != null ? findTableBody.childrenSize() : 0;
    }

    @Override
    public <T> List<T> classFrom(Class<T> clazz) {
        return null;
    }

    @Override
    public <T> List<T> classFrom(int headerRow, Class<T> clazz) {
        return null;
    }

    @Override
    public <R> List<R> cellMap(Function<Cells, R> apply) {
        return null;
    }

    @Override
    public <T> List<T> cellMap(int headerRow, Function<Cells, T> apply) {
        return null;
    }

    @Override
    public void cellForEach(Consumer<Cells> consumer) {

    }

    @Override
    public void cellForEach(int headerRow, Consumer<Cells> consumer) {

    }

    @Override
    public List<CellValue> row(int row) {
        return null;
    }

    @Override
    public List<CellValue> row(int headerRow, int row) {
        return null;
    }

    @Override
    public CellValue cell(int row, int col) {
        return null;
    }

    @Override
    public String value(int row, int col) {
        return null;
    }
}
