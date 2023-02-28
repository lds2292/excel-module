package sfn.excel.module.kenya.validator;

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
    protected ValidateResult validateValue(String value, int rowIndex, int columnIndex, String headerName) {
        return value.isBlank()
            ? new ValidateResult(rowIndex, columnIndex, headerName, value, errorMessage)
            : null;
    }
}
