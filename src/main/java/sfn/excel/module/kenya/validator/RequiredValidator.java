package sfn.excel.module.kenya.validator;

import java.util.List;
import sfn.excel.module.kenya.Row;

public class RequiredValidator extends AbstractCustomValidator {

    public RequiredValidator() {
    }

    public RequiredValidator(String... headerNames) {
        super(headerNames);
    }

    public RequiredValidator(Integer... columnIndex) {
        super(columnIndex);
    }

    @Override
    final protected List<ValidateResult> validateColumnIndex(int rowIndex, Row row) {
        return columnIndexValidate(columnIndex -> {
            String headerName = row.getColumnNames().get(columnIndex);
            return row.getString(columnIndex).isBlank()
                ? new ValidateResult(rowIndex, columnIndex, headerName, row.getString(columnIndex),
                errorMessage)
                : null;
        });
    }

    @Override
    final protected List<ValidateResult> validateHeaderName(int rowIndex, Row row) {
        return headerNameValidate(headerName -> {
            int columnIndex = row.getColumnNames().indexOf(headerName);
            return row.getString(headerName).isBlank()
                ? new ValidateResult(rowIndex, columnIndex, headerName, row.getString(headerName),
                errorMessage)
                : null;
        });
    }
}
