package sfn.excel.module.kenya;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Objects;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ExcelReaderTest {
    @Test
    @DisplayName("암호가 존재하는 엑셀을 열수 있다")
    void passwordFileOpenCheck() throws IOException {
        String password = "1234";
        InputStream fileStream = this.getClass().getResourceAsStream("/ExcelPasswordTest.xlsx");
        try(FileReader reader = new ExcelReader(fileStream, password).init()) {
            reader.document();
        }
    }

    @Test
    @DisplayName("엑셀파일 열기")
    void test() throws URISyntaxException, IOException {
        String password = "1234";
        File file = new File(Objects.requireNonNull(this.getClass().getResource("/ExcelPasswordTest.xlsx")).toURI());
        try(FileReader reader = FileReaderFactory.build(file, password)){
            reader.document().cellForEach(System.out::println);
        }

        file = new File(Objects.requireNonNull(this.getClass().getResource("/ssg.xls")).toURI());
        try(FileReader reader = FileReaderFactory.build(file)){
            reader.document().cellForEach(System.out::println);
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