package sfn.excel.module.kenya;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class SheetReaderActionTest {
    @Test
    @DisplayName("모든셀 확인")
    void typeReadRepeat() throws IOException {
        InputStream fileStream = this.getClass().getResourceAsStream("/Test13Rows.xlsx");
        try(fileStream) {
            SheetReader sheet = new ExcelReader(fileStream).init().sheet();

            List<TestDataModel> result = sheet.rowMap(cells -> new TestDataModel(
                    cells.getString("구분"),
                    cells.getString("그룹"),
                    cells.get(4).toString(),
                    cells.get(7).toString(),
                    cells.get(8).toString(),
                    cells.get(9).toInt(),
                    cells.get("유통기한").toLocalDate()
                )
            );

            result.forEach(System.out::println);
        }
    }

    @Test
    @DisplayName("특정셀 체크")
    void readTest() throws IOException {
        InputStream fileStream = this.getClass().getResourceAsStream("/Test13Rows.xlsx");
        try(fileStream) {
            SheetReader sheet = new ExcelReader(fileStream).init().sheet();

            List<String> kinds = sheet.rowMap(cells -> cells.get("구분").toString());
            assertThat(kinds.size()).isEqualTo(12);
        }
    }

    @Test
    @DisplayName("리플렉션 확인")
    void reflectionCheck() throws IOException {
        InputStream fileStream = this.getClass().getResourceAsStream("/Test13Rows.xlsx");
        try(fileStream) {
            SheetReader sheet = new ExcelReader(fileStream).init().sheet();

            List<TestDataModel> result = sheet.classFrom(TestDataModel.class);
            result.forEach(System.out::println);
        }
    }
}
