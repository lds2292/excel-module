package sfn.excel.module.kenya;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.InputStream;
import java.util.List;
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
    @DisplayName("모든셀 확인")
    void typeReadRepeat() {
        InputStream fileStream = this.getClass().getResourceAsStream("/Test13Rows.xlsx");
        SheetReader sheet = new ExcelReader(fileStream).init().sheet();

        List<TestDataModel> result = sheet.run(cells -> new TestDataModel(
                        cells.getString("구분"),
                        cells.getString("그룹"),
                        cells.get(4).toString(),
                        cells.get(7).toString(),
                        cells.get(8).toString(),
                                cells.get(9).toInt(),
            cells.get("유통기한").toLocalDateTime(null)
            )
        );

        result.forEach(System.out::println);
    }

    @Test
    @DisplayName("리플렉션 확인")
    void reflectionCheck() {
        InputStream fileStream = this.getClass().getResourceAsStream("/Test13Rows.xlsx");
        SheetReader sheet = new ExcelReader(fileStream).init().sheet();

        List<TestDataModel> result = sheet.run(TestDataModel.class);
        result.forEach(System.out::println);
    }

}