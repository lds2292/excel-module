package sfn.excel.module.kenya.validator;

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
    final protected boolean validateValue(String value) {
        if (value.isBlank()) return true;
        try {
            Double.parseDouble(value);
            return false;
        } catch (NumberFormatException e) {
            return true;
        }
    }

}
