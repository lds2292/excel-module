package sfn.excel.module.kenya.validator;

public class ValidateResult {
    private final int rowIndex;

    private final int colIndex;

    private final String headerName;

    private final String value;

    private final String message;

    public ValidateResult(int rowIndex, int colIndex, String headerName, String value, String message) {
        this.rowIndex = rowIndex;
        this.colIndex = colIndex;
        this.headerName = headerName;
        this.value = value;
        this.message = message;
    }

    @Override
    public String toString() {
        return "ValidateResult{" +
            "row=" + rowIndex +
            ", col=" + colIndex +
            ", headerName='" + headerName + '\'' +
            ", value='" + value + '\'' +
            ", message='" + message + '\'' +
            '}';
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public int getColIndex() {
        return colIndex;
    }

    public String getHeaderName() {
        return headerName;
    }

    public String getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }
}
