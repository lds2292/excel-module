package sfn.excel.module.models;

import java.util.ArrayList;
import java.util.List;

public class Workbook {
    public String name = "";
    public List<Worksheet> worksheets = new ArrayList<>();

    public Workbook(String name, List<Worksheet> worksheets) {
        this.name = name;
        this.worksheets = worksheets;
    }
}
