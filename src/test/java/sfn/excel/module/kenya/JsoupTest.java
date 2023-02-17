package sfn.excel.module.kenya;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Objects;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;

public class JsoupTest {

    @Test
    void test() throws URISyntaxException, IOException {
        File file = new File(Objects.requireNonNull(this.getClass().getResource("/ssg.xls")).toURI());
        Document doc = Jsoup.parse(file);
        Element body = doc.body();
        Elements tr = body.getElementsByTag("tr");
        for (Element element : tr) {
            Elements td = element.getElementsByTag("td");
            for (Element data : td) {
                System.out.println(data.text());
            }
        }
    }

}
