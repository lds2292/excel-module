package sfn.excel.module.models;

import java.util.ArrayList;
import java.util.List;

public class Worksheet {
    public String name = "";
    public int sheetIndex = 0;
    public List<List<String>> rows = new ArrayList();

    public Worksheet(String name, int sheetIndex, List<List<String>> rows) {
        this.name = name;
        this.sheetIndex = sheetIndex;
        this.rows = rows;
    }
}
