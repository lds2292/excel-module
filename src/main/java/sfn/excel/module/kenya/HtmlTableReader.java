package sfn.excel.module.kenya;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class HtmlTableReader implements DocumentReader {
    private final List<List<CellValue>> body;

    public HtmlTableReader(List<List<CellValue>> body) {
        this.body = body;
    }

    @Override
    public int lastCellCount(int row) {
        return this.body.get(row).size();
    }

    @Override
    public int lastRowCount() {
        return this.body.size();
    }

    @Override
    public <T> List<T> classFrom(Class<T> clazz) {
        return this.classFrom(0, clazz);
    }

    @Override
    public <T> List<T> classFrom(int headerRow, Class<T> clazz) {
        return body.stream().skip(headerRow + 1).map(row -> {
            Cells cells = new Cells(getColumnHeaders(headerRow), row);
            return ClassSupport.createInstance(cells, clazz);
        }).collect(Collectors.toList());
    }

    @Override
    public <R> List<R> cellMap(Function<Cells, R> apply) {
        return this.cellMap(0, apply);
    }

    @Override
    public <T> List<T> cellMap(int headerRow, Function<Cells, T> apply) {
        return body.stream().skip(headerRow + 1).map(row -> {
            Cells cells = new Cells(getColumnHeaders(headerRow), row);
            return apply.apply(cells);
        }).collect(Collectors.toList());
    }

    @Override
    public void cellForEach(Consumer<Cells> consumer) {
        this.cellForEach(0, consumer);
    }

    @Override
    public void cellForEach(int headerRow, Consumer<Cells> consumer) {
        body.stream().skip(headerRow + 1).forEach(row -> {
            Cells cells = new Cells(getColumnHeaders(headerRow), row);
            consumer.accept(cells);
        });
    }

    @Override
    public List<CellValue> row(int row) {
        return this.body.get(row);
    }

    @Override
    public List<CellValue> row(int headerRow, int row) {
        return this.row(row);
    }

    @Override
    public CellValue cell(int row, int col) {
        return this.body.get(row).get(col);
    }

    @Override
    public String value(int row, int col) {
        return this.cell(row, col).toString();
    }

    @Override
    public List<String> getColumnHeaders(int headerRow){
        return body.get(headerRow).stream().map(CellValue::toString).collect(
            Collectors.toList());
    }
}
