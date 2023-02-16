package sfn.excel.module.kenya;

public class DateTypeNormalizer {

    private static final String TIME_POSTFIX = " 00:00:00";

    public static String edit(String datetimeString){
        String replaceValue = datetimeString
            .replace(".", "-")
            .replace("/", "-");

        if (replaceValue.length() != 19){
            replaceValue += TIME_POSTFIX;
        }
        return replaceValue;
    }
}
