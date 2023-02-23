package sfn.excel.module.kenya;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public interface ReaderSupport {
    <T> List<T> classFrom(Class<T> clazz);

    <T> List<T> classFrom(int headerRow, Class<T> clazz);

    <R> List<R> rowMap(Function<Row, R> apply);

    <T> List<T> rowMap(int headerRow, Function<Row, T> apply);

    void rowForEach(Consumer<Row> consumer);

    void rowForEach(int headerRow, Consumer<Row> consumer);
}
