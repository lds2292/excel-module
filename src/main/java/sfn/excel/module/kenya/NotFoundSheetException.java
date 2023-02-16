package sfn.excel.module.kenya;

public class NotFoundSheetException extends RuntimeException {

    private static final String ERROR_MESSAGE = "시트를 찾을 수 없습니다";
    private final Object sheetName;

    public NotFoundSheetException() {
        super(ERROR_MESSAGE);
        this.sheetName = null;
    }

    public NotFoundSheetException(String sheetName) {
        super(ERROR_MESSAGE + " : " + sheetName);
        this.sheetName = sheetName;
    }

    public Object getSheetName() {
        return sheetName;
    }
}
