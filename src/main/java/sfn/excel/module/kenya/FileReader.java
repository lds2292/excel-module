package sfn.excel.module.kenya;

import java.io.Closeable;

public interface FileReader extends Closeable {
    FileReader init() throws FailedReadFileException;
    DocumentReader document();
    DocumentReader document(int index);
    DocumentReader document(String name);
}
