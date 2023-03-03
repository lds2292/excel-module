package sfn.excel.module.kenya;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import sfn.excel.module.kenya.exception.FailedReadFileException;
import sfn.excel.module.kenya.exception.NotFoundSheetException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static sfn.excel.module.kenya.support.DateTypeNormalizer.dateTimeFormatter;

public class ExcelReader implements FileReader {

    public static final int DEFAULT_ROW = 0;
    public static final int DEFAULT_COL = 0;
    public static final int DEFAULT_SHEET_INDEX = 0;

    private final Workbook workBook;
    private List<SheetReader> sheetReaderList = new ArrayList<>();


    public ExcelReader(File file) throws FailedReadFileException {
        this(file, null);
    }

    public ExcelReader(File file, String password) throws FailedReadFileException {
        assert file != null;

        try {
            this.workBook = WorkbookFactory.create(file, password);
        } catch (IOException | EncryptedDocumentException e) {
            throw new FailedReadFileException(e);
        }
    }

    public ExcelReader(InputStream file) throws FailedReadFileException {
        this(file, null);
    }

    public ExcelReader(InputStream file, String password) throws FailedReadFileException {
        assert file != null;
        try {
            this.workBook = WorkbookFactory.create(file, password);
        } catch (IOException | EncryptedDocumentException e) {
            throw new FailedReadFileException(e);
        }
    }

    @Override
    public ExcelReader init() {
        this.sheetReaderList = createSheetReaders(this.workBook);
        return this;
    }

    private List<SheetReader> createSheetReaders(Workbook workBook) {
        List<SheetReader> sheetReaders = new ArrayList<>();
        for (int i = 0; i < workBook.getNumberOfSheets(); i++) {
            sheetReaders.add(new SheetReader(workBook.getSheetAt(i), dateTimeFormatter));
        }
        return sheetReaders;
    }

    @Override
    public SheetReader document() {
        return this.sheet();
    }

    @Override
    public SheetReader document(int index) {
        return this.sheet(index);
    }

    @Override
    public SheetReader document(String name) {
        return this.sheet(name);
    }

    public SheetReader sheet(){
        return this.sheetReaderList.get(DEFAULT_SHEET_INDEX);
    }
    public SheetReader sheet(int index) {
        return this.sheetReaderList.get(index);
    }

    public SheetReader sheet(String sheetName) {
        int sheetIndex = this.workBook.getSheetIndex(sheetName);
        if (sheetIndex == -1 ) throw new NotFoundSheetException(sheetName);
        return this.sheetReaderList.get(sheetIndex);
    }

    @Override
    public void close() throws IOException {
        this.workBook.close();
    }
}
