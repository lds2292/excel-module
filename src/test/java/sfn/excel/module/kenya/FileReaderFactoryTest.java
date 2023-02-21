package sfn.excel.module.kenya;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Objects;
import org.junit.jupiter.api.Test;

public class FileReaderFactoryTest {

    @Test
    void excelFileReader() throws URISyntaxException, IOException {
        File file = new File(
            Objects.requireNonNull(this.getClass().getResource("/ExcelTest.xlsx")).toURI());
        try (FileReader reader = FileReaderFactory.build(file).init()) {
            assertThat(reader).isInstanceOf(ExcelReader.class);
        }
    }

    @Test
    void excelHtmlReader() throws URISyntaxException, IOException {
        File file = new File(
            Objects.requireNonNull(this.getClass().getResource("/ssg2.xlsx")).toURI());
        try (FileReader reader = FileReaderFactory.build(file).init()) {
            assertThat(reader).isInstanceOf(HtmlReader.class);
        }
    }

    @Test
    void unknown() throws URISyntaxException, IOException {
        File file = new File(Objects.requireNonNull(this.getClass().getResource("/emptyHtml.xlsx")).toURI());
        assertThatThrownBy(() -> {
            try (FileReader reader = FileReaderFactory.build(file).init()) {
            }
        }).isInstanceOf(UnsupportedOperationException.class);
    }

}
