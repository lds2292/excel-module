package sfn.excel.module.kenya.exception;

public class InstanceClassException extends RuntimeException{

    private String fieldName;

    private static final String ERROR_MESSAGE = "클래스 변환중 오류가 발생했습니다";

    public InstanceClassException(String fieldName, String message, Throwable cause) {
        super(message+"(Field name : "+fieldName+")", cause);
        this.fieldName = fieldName;
    }

    public InstanceClassException(String message, Throwable cause) {
        super(message, cause);
    }

    public InstanceClassException(Throwable cause) {
        super(cause);
    }

    public String getFieldName() {
        return fieldName;
    }
}
