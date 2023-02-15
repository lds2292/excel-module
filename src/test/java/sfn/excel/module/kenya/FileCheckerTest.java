package sfn.excel.module.kenya;

import java.io.IOException;
import java.io.InputStream;
import org.junit.jupiter.api.Test;

class FileCheckerTest {

    @Test
    void test() throws IOException {
        InputStream fileStream = this.getClass().getResourceAsStream("/ExcelTest.xlsx");
        System.out.println(new FileChecker().isExcelFile(fileStream));
    }

}