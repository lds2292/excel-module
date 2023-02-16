package sfn.excel.module.kenya;

public class TestDataModel {
    private final String kind;
    private final String locationName;
    private final String temperature;
    private final String productName;
    private final String barcode;
    private final Integer box;

    public TestDataModel(String kind, String locationName, String temperature, String productName, String barcode, Integer box) {
        this.kind = kind;
        this.locationName = locationName;
        this.temperature = temperature;
        this.productName = productName;
        this.barcode = barcode;
        this.box = box;
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

    @Override
    public String toString() {
        return "TestDataModel{" +
                "kind='" + kind + '\'' +
                ", locationName='" + locationName + '\'' +
                ", temperature='" + temperature + '\'' +
                ", productName='" + productName + '\'' +
                ", barcode='" + barcode + '\'' +
                ", box=" + box +
                '}';
    }
}
