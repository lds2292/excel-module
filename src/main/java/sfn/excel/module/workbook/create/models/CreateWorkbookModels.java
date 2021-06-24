package sfn.excel.module.workbook.create.models;

import java.util.ArrayList;
import java.util.List;

public class CreateWorkbookModels {

    public static class Workbook {
        public List<Worksheet> worksheets = new ArrayList<>();
    }

    public static class Worksheet {
        public String worksheetName = "";

        // header("이름", "주소", "순번", ...)
        public List<String> columnHeaderDisplayNames = new ArrayList<>();

        // values grouped by row ([[1,2,3,4,...],["a", "b", "c", "d",...]])
        public List<List<Object>> rows = new ArrayList<>();

        // summary row type (null, "sum", "product", ...)
        public List<String> summaryRowTypes = new ArrayList<>();
    }
}
