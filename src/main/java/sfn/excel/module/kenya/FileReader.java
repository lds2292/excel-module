package sfn.excel.module.kenya;

import java.io.Closeable;
import sfn.excel.module.kenya.exception.FailedReadFileException;

public interface FileReader extends Closeable {
    FileReader init() throws FailedReadFileException;
    DocumentReader document();
    DocumentReader document(int index);
    DocumentReader document(String name);
}
