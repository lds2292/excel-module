package sfn.excel.module.models;

import java.util.ArrayList;
import java.util.List;

public class Worksheet {
    public String name = "";
    public int sheedIndex = 0;
    public List<List<String>> rows = new ArrayList();

    public Worksheet(String name, int sheedIndex, List<List<String>> rows) {
        this.name = name;
        this.sheedIndex = sheedIndex;
        this.rows = rows;
    }
}
