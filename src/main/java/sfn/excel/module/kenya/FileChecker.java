package sfn.excel.module.kenya;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class FileChecker {

    private final int EXCEL_SIGNATURE_SIZE = 4;
    private final int EXCEL_PASSWORD_SIGNATURE_SIZE = 4;
    private final byte[] EXCEL_FILE_SIGNATURE = {0x50, 0x4b, 0x03, 0x04};


    // TODO : 여기서부터 시작
    public boolean isExcelFile(InputStream fileInputStream) throws IOException {
        byte[] bytes = fileInputStream.readNBytes(EXCEL_SIGNATURE_SIZE);
        System.out.println(Arrays.toString(bytes));

        byte[] bytes2 = fileInputStream.readNBytes(EXCEL_SIGNATURE_SIZE);
        System.out.println(Arrays.toString(bytes2));

        return true;
//        return Arrays.equals(EXCEL_FILE_SIGNATURE, bytes);
    }

}
