package sfn.excel.module.kenya;

import java.io.IOException;
import java.io.InputStream;

public class FileChecker {

    private final int EXCEL_SIGNATURE_SIZE = 4;
    private final int EXCEL_PASSWORD_SIGNATURE_SIZE = 4;
    private final byte[] EXCEL_FILE_SIGNATURE = {0x50, 0x4b, 0x03, 0x04};
    private final byte[] EXCEL_PASSWORD_FILE_SIGNATURE = {
        (byte) 0xD0, (byte) 0xCF, (byte) 0x11, (byte) 0xE0,
        (byte) 0xA1, (byte) 0xB1, (byte) 0x1A, (byte) 0xE1
    };


    // TODO : 여기서부터 시작
    public boolean isExcelFile(InputStream fileInputStream) throws IOException {
        byte[] bytes = fileInputStream.readNBytes(EXCEL_SIGNATURE_SIZE);

    }

}
