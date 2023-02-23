package sfn.excel.module.kenya.validator;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import sfn.excel.module.kenya.Row;

public class ValidatorChain implements RowValidator {

    public ValidatorChain() {
    }

    public ValidatorChain(RowValidator validator) {
        this.validatorChain.add(validator);
    }

    private final List<RowValidator> validatorChain = new ArrayList<>();

    public ValidatorChain addValidator(RowValidator validator) {
        this.validatorChain.add(validator);
        return this;
    }

    @Override
    public List<ValidateResult> validate(int rowIndex, Row row) {
        return validatorChain.stream().flatMap(validator -> validator.validate(rowIndex, row).stream()).collect(Collectors.toList());
    }
}
