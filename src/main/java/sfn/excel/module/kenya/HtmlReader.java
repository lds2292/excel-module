package sfn.excel.module.kenya;

import java.io.File;
import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class HtmlReader implements FileReader{

    private final File file;
    private Document document;

    public HtmlReader(File file) {
        this.file = file;
    }

    @Override
    public HtmlReader init() {
        try {
            this.document = Jsoup.parse(this.file);
        } catch (IOException e) {
            throw new FailedReadFileException(e);
        }
        return this;
    }

    @Override
    public HtmlTableReader document() {
        return new HtmlTableReader(document.body());
    }

    @Override
    public HtmlTableReader document(int index) {
        return this.document();
    }

    @Override
    public HtmlTableReader document(String name) {
        return this.document();
    }
}
