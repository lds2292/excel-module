package sfn.excel.module.kenya;

import org.junit.jupiter.api.Test;

public class DateRegExprTest {

    @Test
    void RegExpr(){
        String date = "20230101";
        StringBuffer stringBuffer = new StringBuffer(date);
        stringBuffer
            .insert(4, "-")
            .insert(7, "-");

        String datetime = "20230101010101";

        stringBuffer = new StringBuffer(datetime);
        stringBuffer
            .insert(4, "-")
            .insert(7, "-")
                .insert(10, " ")
                    .insert(13, ":")
            .insert(16, ":");

        System.out.println(stringBuffer);

    }

}
