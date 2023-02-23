package sfn.excel.module.kenya.validator;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import sfn.excel.module.kenya.Row;

public abstract class AbstractCustomValidator implements RowValidator {

    public AbstractCustomValidator() {
    }

    public AbstractCustomValidator(String... headerNames) {
        this.addHeaderName(headerNames);
    }

    public AbstractCustomValidator(Integer... columnIndex) {
        this.addColumnIndex(columnIndex);
    }

    protected final Set<String> headerNames = new HashSet<>();
    protected final Set<Integer> columnIndexes = new HashSet<>();
    protected String errorMessage = "필수값이 누락 되었습니다";

    public Set<Integer> getColumnIndexes() {
        return new HashSet<>(this.columnIndexes);
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void addColumnIndex(Integer... columnIndex) {
        this.columnIndexes.addAll(List.of(columnIndex));
    }

    public void addHeaderName(String... headerNames) {
        this.headerNames.addAll(List.of(headerNames));
    }

    public Set<String> getHeaderNames() {
        return new HashSet<>(this.headerNames);
    }

    public void changeErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public List<ValidateResult> validate(int rowIndex, Row row) {
        if (!columnIndexes.isEmpty()) {
            return validateColumnIndex(rowIndex, row);
        } else {
            return validateHeaderName(rowIndex, row);
        }
    }

    protected List<ValidateResult> columnIndexValidate(Function<Integer, ValidateResult> func){
        return columnIndexes.stream()
            .map(func)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }

    protected List<ValidateResult> headerNameValidate(Function<String, ValidateResult> func){
        return headerNames.stream()
            .map(func)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }

    abstract protected List<ValidateResult> validateColumnIndex(int rowIndex, Row row);
    abstract protected List<ValidateResult> validateHeaderName(int rowIndex, Row row);
}
