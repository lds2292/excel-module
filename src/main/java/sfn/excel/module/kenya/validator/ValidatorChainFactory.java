package sfn.excel.module.kenya.validator;

public class ValidatorChainFactory {

    public static ValidatorChain withRequiredValidator(String... headerNames) {
        RequiredValidator requiredValidator = new RequiredValidator();
        requiredValidator.addHeaderName(headerNames);
        return new ValidatorChain().addValidator(requiredValidator);
    }

    public static ValidatorChain withRequiredValidator(Integer... columnIndexes) {
        RequiredValidator requiredValidator = new RequiredValidator();
        requiredValidator.addColumnIndex(columnIndexes);
        return new ValidatorChain().addValidator(requiredValidator);
    }
}
