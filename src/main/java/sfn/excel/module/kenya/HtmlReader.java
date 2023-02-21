package sfn.excel.module.kenya;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class HtmlReader implements FileReader{

    private static final String TAG_TABLE_BODY = "tbody";
    private static final String TAG_TR = "tr";
    private static final String TAG_TD = "td";

    private final File file;
    private DocumentReader document;

    public HtmlReader(File file) {
        this.file = file;
    }

    @Override
    public HtmlReader init() {
        try {
            Document document = Jsoup.parse(this.file);
            Element tbody = document.getElementsByTag(TAG_TABLE_BODY).first();
            assert tbody != null;
            this.document = new HtmlTableReader(createColumns(tbody));
        } catch (IOException e) {
            throw new FailedReadFileException(e);
        }
        return this;
    }

    @Override
    public HtmlTableReader document() {
        return (HtmlTableReader) this.document;
    }

    @Override
    public HtmlTableReader document(int index) {
        return this.document();
    }

    @Override
    public HtmlTableReader document(String name) {
        return this.document();
    }

    @Override
    public void close() throws IOException {
    }

    private List<List<CellValue>> createColumns(Element tbody) {
        return tbody.getElementsByTag(TAG_TR)
            .stream().map(tr ->
                tr.children().stream().map(td -> new CellValue(td.text())
                ).collect(Collectors.toList())
            ).collect(Collectors.toList());
    }
}
