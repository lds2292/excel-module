package sfn.excel.module.kenya;

public class WriteConfig {
    private final short headerRowHeight;
    private final HeaderStyle headerStyle;

    public WriteConfig(short headerRowHeight, HeaderStyle headerStyle) {
        this.headerRowHeight = (short) (headerRowHeight * 20);
        this.headerStyle = headerStyle;
    }

    public short getHeaderRowHeight() {
        return headerRowHeight;
    }

    public HeaderStyle getHeaderStyle() {
        return headerStyle;
    }

    public static WriteConfig Default() {
        return new WriteConfig((short) 20, new DefaultHeaderStyle());
    }


}
