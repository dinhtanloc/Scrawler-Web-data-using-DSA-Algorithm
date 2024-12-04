package ueh.service;

import ueh.model.HtmlData;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

@Service
public class HtmlFilterService {

    @Autowired
    private QueueService queueService;

    /**
     * Phân loại các thẻ HTML chứa nội dung và đẩy vào queue.
     */
    public void classifyAndEnqueueContent(String rawHtml) {
        Document document = Jsoup.parse(rawHtml);
        Elements contentElements = document.select("title, p, h1, h2, h3, h4, h5, h6, div, span");

        for (Element element : contentElements) {
            HtmlData htmlData = new HtmlData(element.outerHtml());
            queueService.enqueue(htmlData);
        }
    }

    /**
     * Xử lý các thẻ HTML trong queue.
     */
    public String processQueue() {
        StringBuilder processedContent = new StringBuilder();

        while (!queueService.isEmpty()) {
            HtmlData dequeuedHtmlData = queueService.dequeue();
            processedContent.append(dequeuedHtmlData.getRawHtml());
        }

        return processedContent.toString();
    }

    public Map<String, Object> classifyContent(String rawHtml) {
        Map<String, Object> tagContentMap = new HashMap<>();
        Document document = Jsoup.parse(rawHtml);

        String[] tags = {"title", "p", "h1", "h2", "h3", "h4", "h5", "h6", "div", "span"};

        for (String tag : tags) {
            Elements elements = document.select(tag);

            for (Element element : elements) {
                String content = element.text().trim();

                if (!content.isEmpty()) {
                    if (tagContentMap.containsKey(tag)) {
                        Object existingValue = tagContentMap.get(tag);

                        if (existingValue instanceof List) {
                            @SuppressWarnings("unchecked")
                            List<String> contentList = (List<String>) existingValue;
                            contentList.add(content);
                        } else {
                            List<String> contentList = new ArrayList<>();
                            contentList.add((String) existingValue);
                            contentList.add(content);
                            tagContentMap.put(tag, contentList);
                        }
                    } else {
                        tagContentMap.put(tag, content);
                    }
                }
            }
        }

        return tagContentMap;
    }
}
