package sfn.excel.module.kenya;

public class DateTypeNormalizer {

    private static final String TIME_POSTFIX = " 00:00:00";
    private static final String NON_SEPARATOR_DATETIME_FORMAT = "yyyyMMddHHmmss";
    private static final String NON_SEPARATOR_DATE_FORMAT = "yyyyMMdd";

    public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT = "yyyy-MM-dd";

    public static String edit(String datetimeString){
        String replaceValue = datetimeString
            .replace(".", "-")
            .replace("/", "-");

        if (replaceValue.length() == NON_SEPARATOR_DATETIME_FORMAT.length()) {
            return new StringBuffer(replaceValue).insert(4, "-")
                .insert(7, "-")
                .insert(10, " ")
                .insert(13, ":")
                .insert(16, ":").toString();
        }

        if (replaceValue.length() == NON_SEPARATOR_DATE_FORMAT.length()) {
            replaceValue = new StringBuffer(replaceValue)
                .insert(4, "-")
                .insert(7, "-").toString();
        }

        if (replaceValue.length() != 19){
            replaceValue += TIME_POSTFIX;
        }
        return replaceValue;
    }

}
