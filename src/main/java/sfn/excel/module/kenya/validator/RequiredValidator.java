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
    protected boolean validateValue(String value) {
        return value.isBlank();
    }
}
