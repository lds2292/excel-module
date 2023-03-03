package sfn.excel.module.kenya.exception;

public class FailedGetCellValueException extends RuntimeException {
    private int row;
    private int col;

    public FailedGetCellValueException(int row, int col) {
        super(String.format("엑셀 행(%d), 열(%d)의 값을 가져올 수 없습니다. 엑셀의 데이터를 확인하세요", row, col));
        this.row = row;
        this.col = col;
    }

    public FailedGetCellValueException(int row, int col, Throwable cause) {
        super(String.format("엑셀 행(%d), 열(%d)의 값을 가져올 수 없습니다. 엑셀의 데이터를 확인하세요", row, col), cause);
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public String message() {
        return String.format("엑셀 행(%d), 열(%d)의 값을 가져올 수 없습니다. 엑셀의 데이터를 확인하세요", this.row, this.col);
    }
}
