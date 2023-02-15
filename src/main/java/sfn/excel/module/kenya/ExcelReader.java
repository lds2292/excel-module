package sfn.excel.module.kenya;

import java.io.IOException;
import java.io.InputStream;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class ExcelReader {
    public Workbook open(InputStream fileStream) throws IOException {
        return WorkbookFactory.create(fileStream, null);
    }

    public Workbook open(InputStream fileStream, String password) throws IOException {
        return WorkbookFactory.create(fileStream, password);
    }
}
