package sfn.excel.module.kenya;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SheetReaderTest {

    @Test
    @DisplayName("시트열기")
    void openSheet() {
        InputStream fileStream = this.getClass().getResourceAsStream("/ExcelTest.xlsx");
        ExcelReader excel = new ExcelReader(fileStream).init();
        assertThat(excel.sheet().getSheetName()).isEqualTo("시트 1");
        assertThat(excel.sheet("시트 2")).isInstanceOf(SheetReader.class);
    }

    @Test
    @DisplayName("존재하지 않는 시트를 선택한다")
    void invalidSheetName() {
        InputStream fileStream = this.getClass().getResourceAsStream("/ExcelTest.xlsx");
        ExcelReader excel = new ExcelReader(fileStream).init();
        assertThatThrownBy(() -> {
            excel.sheet("시트 3");
        }).isInstanceOf(NotFoundSheetException.class)
                .hasMessageStartingWith("시트를 찾을 수 없습니다");
    }

    @Test
    @DisplayName("로우읽기")
    void readRow() {
        InputStream fileStream = this.getClass().getResourceAsStream("/ExcelTest.xlsx");
        ExcelReader excel = new ExcelReader(fileStream).init();
        assertThat(excel.sheet().value(1, 1)).isEqualTo("B");
    }

    @Test
    @DisplayName("타입별 제대로 읽어오는지 확인")
    void typeRead() {
        InputStream fileStream = this.getClass().getResourceAsStream("/typeExcel.xlsx");
        SheetReader sheet = new ExcelReader(fileStream).init().sheet();
        int rownum = 2;
        for (int i = 0; i < sheet.lastCellNum(rownum); i++) {
            System.out.println(sheet.value(rownum, i));
        }
    }

    @Test
    @DisplayName("CellValue로 제대로 읽어오는지 확인")
    void typeReadFromCellValue() {
        InputStream fileStream = this.getClass().getResourceAsStream("/typeExcel.xlsx");
        SheetReader sheet = new ExcelReader(fileStream).init().sheet();
        assertThat(sheet.cell(0, 0).toString()).isEqualTo("헤더1");
        assertThat(sheet.cell(1, 0).toString()).isEqualTo("2023-01-01 00:00:00");
        assertThat(sheet.cell(1, 1).toDouble()).isEqualTo(44927.0d);
    }

    @Test
    @DisplayName("Date 관련 타입을 제대로 읽어오는지 확인")
    void dateTypeReadTest(){
        InputStream fileStream = this.getClass().getResourceAsStream("/DateExcel.xlsx");
        SheetReader sheet = new ExcelReader(fileStream).init().sheet();

        LocalDateTime expectLocalDate = LocalDate.of(2023, 1, 1).atStartOfDay();
        LocalDateTime expectLocalDateTime = LocalDateTime.of(2023, 1, 1,1,1,1);

        assertThat(sheet.cell(0, 1).toString()).isEqualTo("2023-01-01");
        assertThat(sheet.cell(0, 1).toLocalDateTime()).isEqualTo(expectLocalDate);
        assertThat(sheet.cell(0, 2).toString()).isEqualTo("2023/01/01");
        assertThat(sheet.cell(0, 2).toLocalDateTime()).isEqualTo(expectLocalDate);
        assertThat(sheet.cell(0, 3).toString()).isEqualTo("2023.01.01");
        assertThat(sheet.cell(0, 3).toLocalDateTime()).isEqualTo(expectLocalDate);
        assertThat(sheet.cell(0, 4).toString()).isEqualTo("20230101");
        assertThat(sheet.cell(0, 4).toLocalDateTime()).isEqualTo(expectLocalDate);

        assertThat(sheet.cell(1, 1).toString()).isEqualTo("2023-01-01 01:01:01");
        assertThat(sheet.cell(1, 1).toLocalDateTime()).isEqualTo(expectLocalDateTime);
        assertThat(sheet.cell(1, 2).toString()).isEqualTo("2023/01/01 01:01:01");
        assertThat(sheet.cell(1, 2).toLocalDateTime()).isEqualTo(expectLocalDateTime);
        assertThat(sheet.cell(1, 3).toString()).isEqualTo("2023.01.01 01:01:01");
        assertThat(sheet.cell(1, 3).toLocalDateTime()).isEqualTo(expectLocalDateTime);
        assertThat(sheet.cell(1, 4).toString()).isEqualTo("20230101010101");
        assertThat(sheet.cell(1, 4).toLocalDateTime()).isEqualTo(expectLocalDateTime);
    }

}