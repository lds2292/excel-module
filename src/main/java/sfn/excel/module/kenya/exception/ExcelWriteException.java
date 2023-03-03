package sfn.excel.module.kenya.exception;

public class ExcelWriteException extends RuntimeException {

    private static final String ERROR_MESSAGE = "엑셀생성중 오류가 발생했습니다";

    public ExcelWriteException() {
        super(ERROR_MESSAGE);
    }

    public ExcelWriteException(String message) {
        super(message);
    }

    public ExcelWriteException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExcelWriteException(Throwable cause) {
        super(ERROR_MESSAGE, cause);
    }

    public ExcelWriteException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
