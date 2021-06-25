package sfn.excel.module.workbook.read.models;

import sfn.excel.module.workbook.common.SfnEnums;

import java.util.ArrayList;
import java.util.List;

public class SimpleWorkbookModels {

    public static class Workbook {
        public String name = "";
        public List<Worksheet> worksheets = new ArrayList<>();

        public Workbook(String name, List<Worksheet> worksheets) {
            this.name = name;
            this.worksheets = worksheets;
        }
    }

    public static class Worksheet {
        public String name = "";
        public int sheetIndex = 0;
        public List<SfnEnums.CellType> columnDataTypes;
        public List<List<String>> rows = new ArrayList();

        public Worksheet(String name, int sheetIndex, List<SfnEnums.CellType> columnDataTypes, List<List<String>> rows) {
            this.name = name;
            this.sheetIndex = sheetIndex;
            this.columnDataTypes = columnDataTypes;
            this.rows = rows;
        }
    }
}
