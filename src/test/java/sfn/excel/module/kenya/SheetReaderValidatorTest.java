package sfn.excel.module.kenya;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sfn.excel.module.kenya.validator.NumericValidator;
import sfn.excel.module.kenya.validator.RequiredValidator;
import sfn.excel.module.kenya.validator.ValidateResult;
import sfn.excel.module.kenya.validator.ValidatorChain;
import sfn.excel.module.kenya.validator.ValidatorChainFactory;

class SheetReaderValidatorTest {
    @Test
    @DisplayName("RequiredValidator HeaderName 테스트")
    void requiredValidatorHeaderNameTest() throws IOException {
        InputStream fileStream = this.getClass().getResourceAsStream("/typeExcel.xlsx");
        try(fileStream) {
            SheetReader sheet = new ExcelReader(fileStream).init().sheet();

            ValidatorChain validatorChain = ValidatorChainFactory.withRequiredValidator("헤더1", "헤더4");
            List<ValidateResult> validate = sheet.validate(validatorChain);

            assertThat(validate.size()).isEqualTo(14);
            assertThat(validate).extracting("message").containsOnly("필수값이 누락 되었습니다");
        }
    }

    @Test
    @DisplayName("RequiredValidator ColumnIndex 테스트")
    void requiredValidatorColumnIndexTest() throws IOException {
        InputStream fileStream = this.getClass().getResourceAsStream("/typeExcel.xlsx");
        try(fileStream) {
            SheetReader sheet = new ExcelReader(fileStream).init().sheet();

            ValidatorChain validatorChain = ValidatorChainFactory.withRequiredValidator(0);
            List<ValidateResult> validate = sheet.validate(validatorChain);
            assertThat(validate.size()).isEqualTo(7);
            assertThat(validate).extracting("message").containsOnly("필수값이 누락 되었습니다");
        }
    }

    @Test
    @DisplayName("NumericValidator ColumnIndex 테스트")
    void numericValidatorTest() throws IOException {
        InputStream fileStream = this.getClass().getResourceAsStream("/typeExcel.xlsx");
        try(fileStream) {
            SheetReader sheet = new ExcelReader(fileStream).init().sheet();

            NumericValidator numericValidator = new NumericValidator(0);

            List<ValidateResult> validate = sheet.validate(numericValidator);
            assertThat(validate.size()).isEqualTo(9);
            assertThat(validate.get(0).getValue()).isEqualTo("2023-01-01 00:00:00");
            assertThat(validate).extracting("message").containsOnly("숫자값이 아닙니다");
        }
    }

    @Test
    @DisplayName("RequiredValidator ColumnIndex 테스트")
    void requiredValidatorTest() throws IOException {
        InputStream fileStream = this.getClass().getResourceAsStream("/typeExcel.xlsx");
        try(fileStream) {
            SheetReader sheet = new ExcelReader(fileStream).init().sheet();

            RequiredValidator requiredValidator = new RequiredValidator(0);

            List<ValidateResult> validate = sheet.validate(requiredValidator);
            assertThat(validate.size()).isEqualTo(7);
            assertThat(validate).extracting("message").containsOnly("필수값이 누락 되었습니다");
        }
    }

}