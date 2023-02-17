package sfn.excel.module.kenya;

public class FailedParseException extends RuntimeException{

    private static final String ERROR_MESSAGE = "변환중 오류가 발생했습니다";

    public FailedParseException(String message, Throwable cause) {
        super(message, cause);
    }
}
