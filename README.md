# SFN-Excel-Module

엑셀을 편하게 사용하기 위해 [Apache POI](https://poi.apache.org/)를 Wrapping하여 만든 모듈입니다.

HTML Parser는 Jsoup을 사용했습니다

## Usage

모든 행, 열의 시작은 **0** 부터 시작합니다

## Dependency

```kotlin
repositories {
    maven(url = "https://repo.smartfoodnet.com/repository/maven/")
}

dependencies {
    implementation("sfn", "excel-module", "1.1.4")
}
```

## Support Factory

파일 시그니처를 확인하여 ExcelReader 또는 HtmlReader를 반환합니다 

```java
    try (FileReader reader = FileReaderFactory.build(file).init()) {
        reader.document(); // (=ExcelReader.sheet())
    }
```

## Read Excel File
```java
    ExcelReader excel = new ExcelReader(fileStream).init();
```

## Get WorkBook
```java
    ExcelReader excel = new ExcelReader(fileStream).init();
    Workbook workbook = excel.getWorkBook();
```

## Get Sheet
```java
    SheetReader sheet = excel.sheet();  // 첫번째 시트가져오기
    SheetReader sheet = excel.sheet(0); // 첫번째 시트가져오기
    SheetReader sheet = excel.sheet(1); // 두번째 시트가져오기
    SheetReader sheet = excel.sheet("Sheet Name");   // 시트명으로 가져오기
```

## Get Value From Row, Col

value(row, col)로 가져오는 모든 값은 String으로 가져옵니다

```java
    SheetReader sheet = excel.sheet();
    sheet.value(0, 0);  // 첫번째 행, 첫번째 열 값 가져오기
```

cell(row, col)로 가져오는 값은 다음과 같이 가능합니다

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

## Functions

엑셀은 보통 헤더(=타이틀)을 통해 입력되는 일이 많기 때문에 모든 열을 순회하여 작업할수 있는 함수를 제공합니다

### rowMap

```java
    SheetReader sheet = new ExcelReader(file).init().sheet();

    List<TestDataModel> result = sheet.rowMap(row -> new TestDataModel(
                    row.getString("구분"),
                    row.getString("그룹"),
                    row.get(4).toString(),
                    row.get(7).toString(),
                    row.get(8).toString(),
                            row.get(9).toInt(),
        row.get("유통기한").toLocalDate()
        )
    );
```

### rowForEach
```java
    SheetReader sheet = new ExcelReader(fileStream).init().sheet();

    sheet.rowForEach( row -> {
        System.out.println(row.get("판매자상품번호").toString());
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

    @NumericColumn(headerName = "박스입수")
    private Integer box;

    @StringColumn(headerName = "유통기한")
    private LocalDate expireDate;
}
```

데이터 모델을 만든 이후 다음과 같이 사용합니다.

```java
SheetReader sheet = new ExcelReader(file).init().sheet();
List<TestDataModel> result = sheet.rowMap(TestDataModel.class);
```

## Validator

각 열에 대해 유효성 검사를 할 수 있는 함수를 제공합니다. `validate` 함수를 호출하게 되면 문제가 발생한 행과 열, 값, 메세지가 들어있는 `ValidateResult`객체가 리스트로 반환됩니다.

Validator를 생성후 `addColumnIndex` 또는 `addHeaderName`으로 검사할 열을 지정할 수 있습니다. `columnIndex`를 지정하게 되면 `headerName`의 조건을 입력해도 `columnIndex`로 검사합니다.

`changeErrorMessage`로 오류 메세지를 변경할 수 있습니다.

아래코드는 필수 입력 체크 `Validator`로 첫번째 열(0)에 대해 필수로 값이 입력됨을 확인 합니다.

```java
SheetReader sheet = new ExcelReader(fileStream).init().sheet();

RequiredValidator requiredValidator = new RequiredValidator();
requiredValidator.addColumnIndex(0);

List<ValidateResult> validate = sheet.validate(requiredValidator);
```

| 컬럼1 | 컬럼2 | 컬럼3  | 컬럼4 |
|-----|-----|-----|-----|
| 값1  |     |     |     |
|   |     |     |     |
|     |     |     |     |

위와 같은 엑셀이 있다면 아래의 결과 값은 다음과 같습니다.
```text
ValidateResult{row=2, col=0, headerName='컬럼1', value='', message='필수값이 누락 되었습니다'}
ValidateResult{row=3, col=0, headerName='컬럼1', value='', message='필수값이 누락 되었습니다'}
```

기본적으로 제공되는 `Validator`는 다음과 같습니다.

### RequiredValidator

필수로 값이 입력되어야하는 열을 검사합니다.

기본 에러메세지는 `필수값이 누락 되었습니다`로 표시됩니다.

### NumericValidator

값이 숫자형인지 검사합니다

기본 에러메세지는 `숫자값이 아닙니다`로 표시됩니다.

### CustomValidator

기본으로 제공하는 Validator 이외에 직접 유효성 검사기를 만들어야한다면, `AbstractCustomValidator`을 확장하여 `validateValue` 메서드를 구현합니다.

`validateValue` 메서드의 반환값이 `true`라면 에러를 반환하게 됩니다.

아래코드는 조건으로 입력한 열이 값1인지 체크하는 유효성 검사기 입니다.

```java
public class CustomValidator extends AbstractCustomValidator{

    public CustomValidator() {
        this.errorMessage = "사용자 에러 메세지";
    }

    @Override
    final protected boolean validateValue(String value) {
        return value != "값1";
    }
}
```

## ValidatorChain

행에 대해서 여러가지 유효성을 검사해야한다면 `ValidatorChain`을 사용하면 됩니다. RowValidator를 구현한 메서드를 등록하여 순차적으로 검사할 수 있습니다.

`Factory`를 사용하여 `RequiredValidator`를 기본으로 반환받을 수 있습니다.

아래코드는 동일한 기능을 수행합니다.

```java
// 기본 사용
RequiredValidator requiredValidator = new RequiredValidator("컬럼1");
ValidatorChain validatorChain = new ValidatorChain().addValidator(requiredValidator);
List<ValidateResult> validate = sheet.validate(validatorChain);
```

```java
// Factory를 사용
ValidatorChain validatorChain = ValidatorChainFactory.withRequiredValidator("컬럼1");
List<ValidateResult> validate = sheet.validate(validatorChain);
```

`addValidator` 메서드로 유효성 검사기를 추가할 수 있습니다
```java
ValidatorChain validatorChain = ValidatorChainFactory.withRequiredValidator("컬럼1")
    .addValidator(new NumericValidator("컬럼2"));
List<ValidateResult> validate = sheet.validate(validatorChain);
```