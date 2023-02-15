package sfn.excel.module.kenya;

import static org.assertj.core.api.Assertions.assertThat;

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
    void test() throws IOException {
        InputStream fileStream = this.getClass().getResourceAsStream("/ExcelTest.xls");
        Workbook workbook = new ExcelReader().open(fileStream);
        assertThat(workbook).isInstanceOf(HSSFWorkbook.class);
    }

    @Test
    @DisplayName("xlsx파일을 열때는 XSSFWorkbook 클래스로 반환된다")
    void test2() throws IOException {
        InputStream fileStream = this.getClass().getResourceAsStream("/ExcelTest.xlsx");
        Workbook workbook = new ExcelReader().open(fileStream);
        assertThat(workbook).isInstanceOf(XSSFWorkbook.class);
    }

    @Test
    @DisplayName("암호가 존재하는 엑셀을 열수 있다")
    void test3() throws IOException {
        String password = "1234";
        InputStream fileStream = this.getClass().getResourceAsStream("/ExcelPasswordTest.xlsx");
        Workbook workbook = new ExcelReader().open(fileStream, password);
        assertThat(workbook).isInstanceOf(XSSFWorkbook.class);
    }

}