package sfn.excel.module.kenya;

import java.time.LocalDate;
import sfn.excel.module.kenya.annotation.StringColumn;

public class FailedHtmlDataModel {
    @StringColumn(headerName = "출고유형", defaultValue = "기본값")
    private String kind;

    @StringColumn(headerName = "없는필드", defaultValue = "못찾음")
    private String isToday;

    @StringColumn(headerName = "출고기준일", defaultValue = "")
    private LocalDate releaseDate;

    public String getKind() {
        return kind;
    }

    public String getIsToday() {
        return isToday;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    @Override
    public String toString() {
        return "HtmlDataModel{" +
            "kind='" + kind + '\'' +
            ", isToday='" + isToday + '\'' +
            ", releaseDate='" + releaseDate + '\'' +
            '}';
    }
}
