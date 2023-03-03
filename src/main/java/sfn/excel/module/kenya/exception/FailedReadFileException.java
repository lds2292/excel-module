package sfn.excel.module.kenya.exception;

public class FailedReadFileException extends RuntimeException {

    private static final String ERROR_MESSAGE = "파일을 찾을 수 없거나, 읽기에 실패 했습니다";

    public FailedReadFileException() {
        super(ERROR_MESSAGE);
    }

    public FailedReadFileException(Throwable cause) {
        super(ERROR_MESSAGE, cause);
    }
}
