package sfn.excel.module.kenya;

import sfn.excel.module.kenya.validator.RowValidator;
import sfn.excel.module.kenya.validator.ValidateResult;

import java.util.List;

public interface DocumentValidate {
    List<ValidateResult> validate(RowValidator validator);

    List<ValidateResult> validate(int headerRow, RowValidator validator);
}
