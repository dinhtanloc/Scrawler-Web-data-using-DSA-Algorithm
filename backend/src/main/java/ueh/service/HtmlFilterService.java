package ueh.service;

import ueh.model.HtmlData;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Stack;
import ueh.util.Queue;

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


    // public boolean validateHtmlContent(String rawHtml) {
    //     if (rawHtml == null || rawHtml.isEmpty()) {
    //         return false;
    //     }

    //     Stack<String> tagStack = new Stack<>();
    //     boolean insideTag = false;
    //     StringBuilder currentTag = new StringBuilder();

    //     for (int i = 0; i < rawHtml.length(); i++) {
    //         char currentChar = rawHtml.charAt(i);

    //         if (currentChar == '<') {
    //             insideTag = true;
    //             currentTag.setLength(0);
    //         }

    //         if (insideTag) {
    //             currentTag.append(currentChar);
    //         }

    //         if (currentChar == '>') {
    //             insideTag = false;
    //             String tag = currentTag.toString().trim();

    //             if (tag.startsWith("</")) {
    //                 if (tagStack.isEmpty()) {
    //                     return false;
    //                 }

    //                 String openingTag = tagStack.pop();
    //                 String closingTag = tag.substring(2, tag.length() - 1).trim();
    //                 if (!openingTag.equals(closingTag)) {
    //                     return false;
    //                 }

    //             } else if (tag.startsWith("<") && !tag.endsWith("/>")) {
    //                 String openingTag = tag.substring(1, tag.length() - 1).trim();
    //                 tagStack.push(openingTag);
    //             }
    //         }
    //     }

    //     return tagStack.isEmpty();
    // }

   



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
