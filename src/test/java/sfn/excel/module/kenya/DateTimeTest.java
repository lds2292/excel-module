package sfn.excel.module.kenya;

import org.junit.jupiter.api.Test;

public class DateTimeTest {

    @Test
    void dateTest(){
        String tempValue = "2022-01-13 13:33:34";
        String aa = "HH:mm:ss";
        System.out.println(tempValue.substring(11, 10+aa.length()));
    }

}
