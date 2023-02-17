package sfn.excel.module.kenya;

public interface FileReader {
    FileReader init() throws FailedReadFileException;
    DocumentReader document();
    DocumentReader document(int index);
    DocumentReader document(String name);
}
