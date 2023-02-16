package sfn.excel.module.kenya;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class ExcelReader {

    public static final int FIRST_ROW = 0;
    public static final int FIRST_COL = 0;
    public static final int FIRST_SHEET = 0;

    private final Workbook workBook;
    private List<SheetReader> sheetReaderList = new ArrayList<>();

    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(
        "yyyy-MM-dd HH:mm:ss");


    public ExcelReader(File file) throws FailedReadFileException {
        assert file != null;

        try {
            this.workBook = WorkbookFactory.create(new FileInputStream(file));
        } catch (IOException e) {
            throw new FailedReadFileException(e);
        }
    }

    public ExcelReader(File file, String password) throws FailedReadFileException {
        assert file != null;

        try {
            this.workBook = WorkbookFactory.create(new FileInputStream(file), password);
        } catch (IOException | EncryptedDocumentException e) {
            throw new FailedReadFileException(e);
        }
    }

    public ExcelReader(InputStream file) throws FailedReadFileException {
        assert file != null;
        try {
            this.workBook = WorkbookFactory.create(file);
        } catch (IOException e) {
            throw new FailedReadFileException(e);
        }
    }

    public ExcelReader(InputStream file, String password) throws FailedReadFileException {
        assert file != null;
        try {
            this.workBook = WorkbookFactory.create(file, password);
        } catch (IOException | EncryptedDocumentException e) {
            throw new FailedReadFileException(e);
        }
    }

    public ExcelReader init() {
        for (int i = 0; i < this.workBook.getNumberOfSheets(); i++) {
            this.sheetReaderList.add(
                new SheetReader(this.workBook.getSheetAt(i), this.dateTimeFormatter)
            );
        }
        return this;
    }

    public Workbook getWorkBook() {
        return workBook;
    }

    public SheetReader sheet(){
        return this.sheetReaderList.get(FIRST_SHEET);
    }
    public SheetReader sheet(int index) {
        return this.sheetReaderList.get(index);
    }

    public SheetReader sheet(String sheetName) {
        int sheetIndex = this.workBook.getSheetIndex(sheetName);
        if (sheetIndex == -1 ) throw new NotFoundSheetException(sheetName);
        return this.sheetReaderList.get(sheetIndex);
    }
}
