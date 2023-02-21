package sfn.excel.module.kenya;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public interface DocumentReader {
    int lastCellCount(int row);

    int lastRowCount();

    <T> List<T> classFrom(Class<T> clazz);

    <T> List<T> classFrom(int headerRow, Class<T> clazz);

    <R> List<R> cellMap(Function<Cells, R> apply);

    <T> List<T> cellMap(int headerRow, Function<Cells, T> apply);

    void cellForEach(Consumer<Cells> consumer);

    void cellForEach(int headerRow, Consumer<Cells> consumer);

    List<CellValue> row(int row);

    List<CellValue> row(int headerRow, int row);

    CellValue cell(int row, int col);

    String value(int row, int col);

    List<String> getColumnHeaders(int headerRow);
}
