package sfn.excel.module.kenya.validator;

import java.util.List;
import sfn.excel.module.kenya.Row;

public class NumericValidator extends AbstractCustomValidator implements RowValidator {

    private static final String ERROR_MESSAGE = "숫자값이 아닙니다";

    public NumericValidator() {
        this.errorMessage = ERROR_MESSAGE;
    }

    public NumericValidator(String... headerNames) {
        super(headerNames);
        this.errorMessage = ERROR_MESSAGE;
    }

    public NumericValidator(Integer... columnIndex) {
        super(columnIndex);
        this.errorMessage = ERROR_MESSAGE;
    }

    @Override
    final protected List<ValidateResult> validateColumnIndex(int rowIndex, Row row) {
        return columnIndexValidate(columnIndex -> {
            String headerName = row.getColumnNames().get(columnIndex);
            String value = row.getString(columnIndex);
            return validateNumeric(value)
                ? new ValidateResult(rowIndex, columnIndex, headerName, value, errorMessage)
                : null;
        });
    }
    @Override
    final protected List<ValidateResult> validateHeaderName(int rowIndex, Row row) {
        return headerNameValidate(headerName -> {
            int columnIndex = row.getColumnNames().indexOf(headerName);
            String value = row.getString(columnIndex);
            return validateNumeric(value)
                ? new ValidateResult(rowIndex, columnIndex, headerName, row.getString(headerName),
                errorMessage)
                : null;
        });
    }

    private boolean validateNumeric(String value) {
        if (value.isBlank()) return true;
        try {
            Double.parseDouble(value);
            return false;
        } catch (NumberFormatException e) {
            return true;
        }

    }

}
