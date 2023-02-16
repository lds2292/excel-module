package sfn.excel.module.kenya;

import java.util.List;

public class Cells {

    private final List<String> columnNames;
    private final List<CellValue> cellValues;

    public Cells(List<String> columnNames, List<CellValue> cellValues) {
        this.columnNames = columnNames;
        this.cellValues = cellValues;
    }

    /**
     * 컬럼명으로 CellValue를 가져옵니다. 중복되는 컬럼명이 존재하는 경우 처음으로 찾는 CellValue를 가져옵니다
     * </p>
     * 만약 중복되는 컬럼명을 사용한다고 하면 {@code get(int Index)}를 사용하세요
     *
     * @param columnName 가져올 컬럼명
     * @return CellValue
     */
    CellValue get(String columnName) {
        int index = this.columnNames.indexOf(columnName);
        if (index == -1) return new CellValue();
        return cellValues.get(index);
    }

    /**
     * index로 CellValue를 가져옵니다.
     *
     * @param index 가져올 인덱스
     * @return CellValue
     */
    CellValue get(int index) {
        return cellValues.get(index);
    }

    /**
     * 컬럼명으로 CellValue를 찾아 문자열로 가져옵니다. 중복되는 컬럼명이 존재하는 경우 처음으로 찾는 CellValue의 문자열을 가져옵니다
     * </p>
     * 만약 중복되는 컬럼명을 사용한다고 하면 {@code get(int Index)}를 사용하세요
     *
     * @param columnName 가져올 컬럼명
     * @return String CellValue의 문자열
     */
    String getString(String columnName) {
        return this.get(columnName).toString();
    }

    public List<String> getColumnNames() {
        return columnNames;
    }

    public List<CellValue> getCellValues() {
        return cellValues;
    }
}
