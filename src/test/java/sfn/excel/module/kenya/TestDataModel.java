package sfn.excel.module.kenya;

import java.time.LocalDateTime;
import sfn.excel.module.kenya.annotation.IntegerColumn;
import sfn.excel.module.kenya.annotation.LocalDateTimeColumn;
import sfn.excel.module.kenya.annotation.StringColumn;

public class TestDataModel {
    @StringColumn(headerName = "구분", defaultValue = "기본값")
    private String kind;

    @StringColumn(headerName = "로케이션명", defaultValue = "기본로케이션")
    private String locationName;

    @StringColumn(headerName = "온도구분", defaultValue = "상온기본")
    private String temperature;

    @StringColumn(headerName = "출고상품명", defaultValue = "상품기본값")
    private String productName;

    @StringColumn(headerName = "바코드", defaultValue = "기본바코드")
    private String barcode;

    @IntegerColumn(headerName = "박스입수")
    private Integer box;

    @LocalDateTimeColumn(headerName = "유통기한")
    private LocalDateTime expireDate;

    private TestDataModel() {
    }

    public TestDataModel(String kind, String locationName, String temperature, String productName,
        String barcode, Integer box, LocalDateTime expireDate) {
        this.kind = kind;
        this.locationName = locationName;
        this.temperature = temperature;
        this.productName = productName;
        this.barcode = barcode;
        this.box = box;
        this.expireDate = expireDate;
    }

    public String getKind() {
        return kind;
    }

    public String getLocationName() {
        return locationName;
    }

    public String getTemperature() {
        return temperature;
    }

    public String getProductName() {
        return productName;
    }

    public String getBarcode() {
        return barcode;
    }

    public Integer getBox() {
        return box;
    }

    public LocalDateTime getExpireDate() {
        return expireDate;
    }

    @Override
    public String toString() {
        return "TestDataModel{" +
            "kind='" + kind + '\'' +
            ", locationName='" + locationName + '\'' +
            ", temperature='" + temperature + '\'' +
            ", productName='" + productName + '\'' +
            ", barcode='" + barcode + '\'' +
            ", box=" + box +
            ", expireDate=" + expireDate +
            '}';
    }
}
