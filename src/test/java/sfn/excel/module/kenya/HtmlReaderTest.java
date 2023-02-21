package sfn.excel.module.kenya;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.Test;

public class HtmlReaderTest {

    @Test
    void notFoundTable() throws URISyntaxException, IOException {
        File file = new File(Objects.requireNonNull(this.getClass().getResource("/emptyHtml.xlsx")).toURI());
        assertThatThrownBy(() -> {
            try (FileReader reader = FileReaderFactory.build(file).init()) {
                DocumentReader document = reader.document();
            }
        }).isInstanceOf(UnsupportedOperationException.class)
            .hasMessage("지원하지 않는 파일입니다");
    }

    @Test
    void reflectionTest() throws URISyntaxException, IOException {
        File file = new File(Objects.requireNonNull(this.getClass().getResource("/ssg2.xlsx")).toURI());
        try(FileReader reader = FileReaderFactory.build(file).init()) {
            DocumentReader document = reader.document();
            List<HtmlDataModel> testDataModels = document.classFrom(HtmlDataModel.class);
            assertThat(testDataModels).extracting("kind").containsOnly("일반출고");
            assertThat(testDataModels).extracting("isToday").containsOnly("미대상");
        }
    }

    @Test
    void failedReflectTest() throws IOException, URISyntaxException {
        File file = new File(Objects.requireNonNull(this.getClass().getResource("/ssg2.xlsx")).toURI());
        try(FileReader reader = FileReaderFactory.build(file).init()) {
            DocumentReader document = reader.document();
            assertThatThrownBy(() -> {
                document.classFrom(FailedHtmlDataModel.class);
            }).isInstanceOf(InstanceClassException.class)
                .hasMessage("Header Name(없는필드)을 찾을 수 없습니다");
        }
    }


}
