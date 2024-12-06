package ueh.service;


import ueh.model.HtmlData;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class HtmlCrawlerService {
    public HtmlData crawl(String url) throws IOException {
        String rawHtml = Jsoup.connect(url).get().html();
        return new HtmlData(url, rawHtml);
    }
}
