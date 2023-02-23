package sfn.excel.module.kenya;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import sfn.excel.module.kenya.validator.RowValidator;
import sfn.excel.module.kenya.validator.ValidateResult;

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
            Row customRow = new Row(getColumnHeaders(headerRow), row);
            return ClassSupport.createInstance(customRow, clazz);
        }).collect(Collectors.toList());
    }

    @Override
    public <R> List<R> rowMap(Function<Row, R> apply) {
        return this.rowMap(0, apply);
    }

    @Override
    public <T> List<T> rowMap(int headerRow, Function<Row, T> apply) {
        return body.stream().skip(headerRow + 1).map(row -> {
            Row customRow = new Row(getColumnHeaders(headerRow), row);
            return apply.apply(customRow);
        }).collect(Collectors.toList());
    }

    @Override
    public void rowForEach(Consumer<Row> consumer) {
        this.rowForEach(0, consumer);
    }

    @Override
    public void rowForEach(int headerRow, Consumer<Row> consumer) {
        body.stream().skip(headerRow + 1).forEach(row -> {
            Row customRow = new Row(getColumnHeaders(headerRow), row);
            consumer.accept(customRow);
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

    @Override
    public List<ValidateResult> validate(RowValidator validator) {
        return this.validate(0, validator);
    }

    @Override
    public List<ValidateResult> validate(int headerRow, RowValidator validator) {

        List<String> columnHeaders = getColumnHeaders(headerRow);
        List<ValidateResult> ret = new ArrayList<>();
        for (int rowIndex = headerRow + 1; rowIndex < body.size(); rowIndex++) {
            Row customRow = new Row(columnHeaders, body.get(rowIndex));
            ret.addAll(validator.validate(rowIndex, customRow));
        }
        return ret;
    }
}
