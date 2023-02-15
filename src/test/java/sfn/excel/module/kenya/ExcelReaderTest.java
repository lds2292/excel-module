package sfn.excel.module.kenya;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.InputStream;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ExcelReaderTest {

    @Test
    @DisplayName("xls파일을 열때는 HSSFWorkbook 클래스로 반환된다")
    void xlsFileOpenClassCheck() {
        InputStream fileStream = this.getClass().getResourceAsStream("/ExcelTest.xls");
        Workbook workbook = new ExcelReader(fileStream).getWorkBook();
        assertThat(workbook).isInstanceOf(HSSFWorkbook.class);
    }

    @Test
    @DisplayName("xlsx파일을 열때는 XSSFWorkbook 클래스로 반환된다")
    void xlsxFileOpenClassCheck() {
        InputStream fileStream = this.getClass().getResourceAsStream("/ExcelTest.xlsx");
        Workbook workbook = new ExcelReader(fileStream).getWorkBook();
        assertThat(workbook).isInstanceOf(XSSFWorkbook.class);
    }

    @Test
    @DisplayName("암호가 존재하는 엑셀을 열수 있다")
    void passwordFileOpenCheck() {
        String password = "1234";
        InputStream fileStream = this.getClass().getResourceAsStream("/ExcelPasswordTest.xlsx");
        Workbook workbook = new ExcelReader(fileStream, password).getWorkBook();
        assertThat(workbook).isInstanceOf(XSSFWorkbook.class);
    }

    @Test
    @DisplayName("엑셀파일 읽기 실패")
    void failsReadFile() {
        assertThatThrownBy(() -> {
            InputStream fileStream = this.getClass().getResourceAsStream("/UnknownFile.xlsx");
            new ExcelReader(fileStream);
        }).isInstanceOf(AssertionError.class);
    }

    @Test
    @DisplayName("시트열기")
    void openSheet(){
        InputStream fileStream = this.getClass().getResourceAsStream("/ExcelTest.xlsx");
        ExcelReader excel = new ExcelReader(fileStream);
        assertThat(excel.sheet(0).getSheetName()).isEqualTo("시트 1");
        assertThat(excel.sheet("시트 2")).isInstanceOf(Sheet.class);
    }

    @Test
    @DisplayName("비어있는 엑셀 열었을경우")
    void emptyExcel(){
        InputStream fileStream = this.getClass().getResourceAsStream("/ExcelBlank.xlsx");
        assertThatThrownBy(()->{
            new SheetReader(new ExcelReader(fileStream).sheet());
        }).isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Sheet Empty!");

    }

    @Test
    @DisplayName("로우읽기")
    void readRow(){
        InputStream fileStream = this.getClass().getResourceAsStream("/ExcelTest.xlsx");
        ExcelReader excel = new ExcelReader(fileStream);
        assertThat(new SheetReader(excel.sheet()).value(1, 1)).isEqualTo("B");
    }

    @Test
    @DisplayName("타입별 제대로 읽어오는지 확인")
    void typeRead(){
        InputStream fileStream = this.getClass().getResourceAsStream("/typeExcel.xlsx");
        ExcelReader excel = new ExcelReader(fileStream);
        SheetReader sheet = new SheetReader(excel.sheet());
        for (int i = 0; i < sheet.lastCellNum(1); i++) {
            System.out.println(sheet.value(1, i));
        }
    }

}