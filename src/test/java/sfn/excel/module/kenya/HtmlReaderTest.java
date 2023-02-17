package sfn.excel.module.kenya;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Objects;
import org.junit.jupiter.api.Test;

public class HtmlReaderTest {

    @Test
    void test() throws URISyntaxException {
        File file = new File(Objects.requireNonNull(this.getClass().getResource("/ssg.xls")).toURI());
        HtmlTableReader document = new HtmlReader(file).init().document();

//        System.out.println(document.lastCellCount(0));
        System.out.println(document.lastRowCount());

    }

}
