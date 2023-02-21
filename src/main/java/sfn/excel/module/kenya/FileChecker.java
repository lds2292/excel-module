package sfn.excel.module.kenya;

import static sfn.excel.module.kenya.FileChecker.FileType.CSV;
import static sfn.excel.module.kenya.FileChecker.FileType.HTML;
import static sfn.excel.module.kenya.FileChecker.FileType.TSV;
import static sfn.excel.module.kenya.FileChecker.FileType.UNKNOWN;
import static sfn.excel.module.kenya.FileChecker.FileType.XLS;
import static sfn.excel.module.kenya.FileChecker.FileType.XLSX;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class FileChecker {
    public static FileType getFileType(File file) throws IOException {
        byte[] header = new byte[4];

        try (FileInputStream inputStream = new FileInputStream(file)) {
            inputStream.read(header, 0, 4);

            if (header[0] == 0x50 && header[1] == 0x4B && header[2] == 0x03 && header[3] == 0x04) {
                return XLSX;
            } else if (header[0] == (byte) 0xD0 && header[1] == (byte) 0xCF && header[2] == 0x11 && header[3] == (byte) 0xE0) {
                return XLS;
            } else if (checkForHtmlDoctype(inputStream)) {
                return HTML;
            } else {
                String fileExtension = file.getName().toLowerCase();
                if (fileExtension.endsWith(".csv")) {
                    return CSV;
                } else if (fileExtension.endsWith(".tsv")) {
                    return TSV;
                } else {
                    return UNKNOWN;
                }
            }
        }
    }

    public static boolean checkForHtmlDoctype(FileInputStream inputStream) throws IOException {
        byte[] buffer = new byte[4096];
        int bytesRead;
        StringBuilder content = new StringBuilder();

        while ((bytesRead = inputStream.read(buffer)) != -1) {
            content.append(new String(buffer, 0, bytesRead, StandardCharsets.UTF_8));
            if (content.toString().contains("<!DOCTYPE") || content.toString().contains("<thead>")) {
                return true;
            } else if (content.length() >= 4096) {
                return false;
            }
        }

        return false;
    }

    public enum FileType {
        CSV,
        TSV,
        XLSX,
        XLS,
        HTML,
        UNKNOWN
    }
}
