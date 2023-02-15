package sfn.excel.module.kenya;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class ExcelReader {

    public static final int FIRST_ROW = 0;
    public static final int FIRST_COL = 0;
    public static final int FIRST_SHEET = 0;

    private Workbook workBook;

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

    public Workbook getWorkBook() {
        return workBook;
    }

    public Sheet sheet(){
        return this.workBook.getSheetAt(FIRST_SHEET);
    }
    public Sheet sheet(int index) {
        return this.workBook.getSheetAt(index);
    }

    public Sheet sheet(String sheetName) {
        return this.workBook.getSheet(sheetName);
    }
}
