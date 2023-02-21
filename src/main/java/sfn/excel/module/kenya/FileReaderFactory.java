package sfn.excel.module.kenya;

import java.io.File;
import java.io.IOException;
import sfn.excel.module.kenya.FileChecker.FileType;

public class FileReaderFactory {
    public static FileReader build(File file) throws IOException {
        return build(file, null);
    }

    public static FileReader build(File file, String password) throws IOException {
        FileType type = FileChecker.getFileType(file);
        switch(type) {
            case XLS:
            case XLSX:
                return new ExcelReader(file, password).init();
            case HTML:
                return new HtmlReader(file).init();
            default:
                throw new UnsupportedOperationException("지원하지 않는 파일입니다");
        }
    }
}
