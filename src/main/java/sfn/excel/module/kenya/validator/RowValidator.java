package sfn.excel.module.kenya.validator;

import sfn.excel.module.kenya.Row;

import java.util.List;

@FunctionalInterface
public interface RowValidator {
    List<ValidateResult> validate(int rowIndex, Row row);
}
