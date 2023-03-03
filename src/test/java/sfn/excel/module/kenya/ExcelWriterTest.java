package sfn.excel.module.kenya;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import sfn.excel.module.kenya.annotation.ExcelExport;
import sfn.excel.module.kenya.exception.ExcelWriteException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class ExcelWriterTest {

    @Test
    @Disabled
    void test() {
        ExcelWriter writer = new ExcelWriter("MySheet");

        List<DataModel> sources = new ArrayList<>();
        for (int i = 0; i < 65535; i++) {
            if (i % 20 == 0)
                sources.add(new DataModel(null, null, null, null, null, null));
            else
                sources.add(new DataModel("이름"+i, "주소"+i, i, (double) (i * 500), LocalDate.now(), LocalDateTime.now()));
        }

        String path = writer.write(sources);
        System.out.println(path);
    }

    @Test
    @Disabled
    void writeConsumerTest(){
        ExcelWriter writer = new ExcelWriter("나의시트");
        String path = writer.write((wb, sheet) -> {
            for (int i = 0; i < 1000; i++){
                Row row = sheet.createRow(i);
                for (int j = 0; j < 10; j++) {
                    Cell cell = row.createCell(j);
                    cell.setCellValue(i +"/"+ j);
                }
            }
        });
    }

    @Test
    void emptyListTest(){
        ExcelWriter writer = new ExcelWriter("MySheet");
        List<DataModel> sources = new ArrayList<>();
        assertThatThrownBy(() -> {
            writer.write(sources);
        }).isInstanceOf(ExcelWriteException.class)
                .hasMessageContaining("sources must not be empty");
    }


    public static class DataModel implements ExcelSupport{
        @ExcelExport(order = 1, name = "이름", defaultValue = "이름 기본값")
        private final String name;
        @ExcelExport(order = 0, name = "주소", defaultValue = "주소 기본값")
        private final String address;

        @ExcelExport(order = 2, name = "수량", defaultValue = "")
        private final Integer quantity;

        @ExcelExport(order = 3, name = "가격", defaultValue = "0")
        private final Double amount;

        @ExcelExport(order = 4, name = "요청일", defaultValue = "0")
        private final LocalDate requestDate;

        @ExcelExport(order = 5, name = "요청일시", defaultValue = "0")
        private final LocalDateTime requestDateTime;

        public DataModel(String name, String address, Integer quantity, Double amount, LocalDate requestDate, LocalDateTime requestDateTime) {
            this.name = name;
            this.address = address;
            this.quantity = quantity;
            this.amount = amount;
            this.requestDate = requestDate;
            this.requestDateTime = requestDateTime;
        }

        public String getName() {
            return name;
        }

        public String getAddress() {
            return address;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public Double getAmount() {
            return amount;
        }

        public LocalDate getRequestDate() {
            return requestDate;
        }

        public LocalDateTime getRequestDateTime() {
            return requestDateTime;
        }
    }
}
