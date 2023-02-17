package sfn.excel.module.kenya;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.IOException;
import java.io.InputStream;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ExcelReaderTest {

    @Test
    @DisplayName("xls파일을 열때는 HSSFWorkbook 클래스로 반환된다")
    void xlsFileOpenClassCheck() throws IOException {
        InputStream fileStream = this.getClass().getResourceAsStream("/ExcelTest.xls");
        try(fileStream) {
            Workbook workbook = new ExcelReader(fileStream).getWorkBook();
            assertThat(workbook).isInstanceOf(HSSFWorkbook.class);
        }
    }

    @Test
    @DisplayName("xlsx파일을 열때는 XSSFWorkbook 클래스로 반환된다")
    void xlsxFileOpenClassCheck() throws IOException {
        InputStream fileStream = this.getClass().getResourceAsStream("/ExcelTest.xlsx");
        try(fileStream) {
            Workbook workbook = new ExcelReader(fileStream).getWorkBook();
            assertThat(workbook).isInstanceOf(XSSFWorkbook.class);
        }
    }

    @Test
    @DisplayName("암호가 존재하는 엑셀을 열수 있다")
    void passwordFileOpenCheck() throws IOException {
        String password = "1234";
        InputStream fileStream = this.getClass().getResourceAsStream("/ExcelPasswordTest.xlsx");
        try(fileStream) {
            Workbook workbook = new ExcelReader(fileStream, password).getWorkBook();
            assertThat(workbook).isInstanceOf(XSSFWorkbook.class);
        }
    }

    @Test
    @DisplayName("엑셀파일 읽기 실패")
    void failsReadFile() {
        assertThatThrownBy(() -> {
            InputStream fileStream = this.getClass().getResourceAsStream("/UnknownFile.xlsx");
            try(fileStream) {
                new ExcelReader(fileStream).init();
            }
        }).isInstanceOf(AssertionError.class);
    }

    @Test
    @DisplayName("비어있는 엑셀 열었을경우")
    void emptyExcel() throws IOException {
        InputStream fileStream = this.getClass().getResourceAsStream("/ExcelBlank.xlsx");
        try(fileStream){
            assertThatThrownBy(() -> {
                new ExcelReader(fileStream).init().sheet();
            }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Sheet Empty!");
        }
    }

}