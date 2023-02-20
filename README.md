# SFN-Excel-Module

엑셀을 편하게 사용하기 위해 [Apache POI](https://poi.apache.org/)를 Wrapping하여 만든 모듈

## Usage

모든 행, 열의 시작은 **0** 부터 시작합니다

### Dependency

```kotlin
repositories {
    maven(url = "https://repo.smartfoodnet.com/repository/maven/")
}

dependencies {
    implementation("sfn", "excel-module", "1.1.2")
}
```

### Read Excel File
```java
    ExcelReader excel = new ExcelReader(fileStream).init();
```

### Get WorkBook
```java
    ExcelReader excel = new ExcelReader(fileStream).init();
    Workbook workbook = excel.getWorkBook();
```

### Get Sheet
```java
    SheetReader sheet = excel.sheet();  // 첫번째 시트가져오기
    SheetReader sheet = excel.sheet(0); // 첫번째 시트가져오기
    SheetReader sheet = excel.sheet(1); // 두번째 시트가져오기
    SheetReader sheet = excel.sheet("Sheet Name");   // 시트명으로 가져오기
```

### Get Value From Row, Col

value(row, col)로 가져오는 모든 값은 String으로 가져온다

```java
    SheetReader sheet = excel.sheet();
    sheet.value(0, 0);  // 첫번째 행, 첫번째 열 값 가져오기
```

cell(row, col)로 가져오는 값은 다음과 같이 가능하다

```java
    SheetReader sheet = excel.sheet();
    CellValue cell = sheet.cell(0, 0);
    // 문자열로 가져오기
    cell.toString();
    // Integer로 가져오기
    cell.toInt();
    // Double로 가져오기
    cell.toDouble();
    // LocalDateTime으로 가져오기
    cell.toLocalDdateTime();
```

### Functions

엑셀은 보통 헤더(=타이틀)을 통해 입력되는 일이 많기 때문에 모든 열을 순회하여 작업할수 있는 함수를 제공합니다

#### CellMap

```java
    SheetReader sheet = new ExcelReader(file).init().sheet();

    List<TestDataModel> result = sheet.cellMap(cells -> new TestDataModel(
                    cells.getString("구분"),
                    cells.getString("그룹"),
                    cells.get(4).toString(),
                    cells.get(7).toString(),
                    cells.get(8).toString(),
                            cells.get(9).toInt(),
        cells.get("유통기한").toLocalDate()
        )
    );
```

#### CellForEach
```java
    SheetReader sheet = new ExcelReader(fileStream).init().sheet();

    sheet.cellForEach( cells -> {
        System.out.println(cells.get("판매자상품번호").toString());
    });
```

다음과 같인 데이터 클래스를 만들고 필드에 어노테이션을 붙임으로서 리플렉션을 통해 값을 바인딩 할 수 있습니다.

| annotation           | 설명            | 
|----------------------|---------------|
| @StringColumn        | 문자형 매핑을 지원합니다 |
| @NumericColumn       | 숫자형 매핑을 지원합니다 |
| @LocalDateTimeColumn | 날짜형매핑을 지원합니다  |

**공통 Attribute**

headerIndex : 엑셀의 열을 지정합니다. 기본값은 -1이며 기본값으로 설정되어있다면 Index를 사용하지 않습니다.

headerName : 엑셀 헤더(=타이틀)이름으로 열을 찾아 매핑합니다. Index가 설정되어 있다면 이 값은 무시됩니다.

defaultValue : 찾지 못할 경우 기본값으로 지정할 값입니다.

policy : headerName을 통해 열을 찾지 못했을때 정책을 지정합니다. 기본값은 ERROR입니다.
```java
public enum NotFoundHeaderNamePolicy {
    /**
     * 헤더명으로 찾지 못한다면 Exception을 발생시킨다
     */
    ERROR,

    /**
     * 헤더명으로 찾지 못한다면 기본값으로 표시한다
     */
    DEFAULT_VALUE
}
```

```java
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

    @StringColumn(headerName = "유통기한")
    private LocalDate expireDate;
}
```

데이터 모델을 만든 이후 다음과 같이 사용합니다.

```java
SheetReader sheet = new ExcelReader(file).init().sheet();
List<TestDataModel> result = sheet.cellMap(TestDataModel.class);
```
