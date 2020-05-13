package xin.justcsl.autocheckin;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

@Component
public class HtmlUtil {

    public String getExecution(String html) {
        Document document = Jsoup.parse(html);
        Element element = document.getElementsByAttributeValue("name", "execution").first();
        return element.attr("value");
    }
}
