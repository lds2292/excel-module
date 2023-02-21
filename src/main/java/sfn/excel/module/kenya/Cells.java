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

    int findColumnNameIndex(String columnName) {
        return this.columnNames.indexOf(columnName);
    }

    public CellValue get(String columnName) {
        int index = findColumnNameIndex(columnName);
        if (index == -1) return new CellValue();
        return cellValues.get(index);
    }

    /**
     * index로 CellValue를 가져옵니다.
     *
     * @param index 가져올 인덱스
     * @return CellValue
     */
    public CellValue get(int index) {
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
    public String getString(String columnName) {
        return this.get(columnName).toString();
    }

    public String getString(int index) {
        return this.get(index).toString();
    }

    public List<String> getColumnNames() {
        return columnNames;
    }

    public List<CellValue> getCellValues() {
        return cellValues;
    }

    @Override
    public String toString() {
        return "Cells{" +
            "cellValues=" + cellValues +
            '}';
    }
}
