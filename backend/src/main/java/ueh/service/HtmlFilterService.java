package ueh.service;

import ueh.model.HtmlData;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Safelist;
import org.springframework.stereotype.Service;

@Service
public class HtmlFilterService {
    public String filterHtml(String rawHtml) {
        Document document = Jsoup.parse(rawHtml);
        return Jsoup.clean(document.body().html(), Safelist.simpleText());
    }
}
