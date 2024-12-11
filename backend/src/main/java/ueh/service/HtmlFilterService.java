package ueh.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ueh.model.HtmlData;

@Service
public class HtmlFilterService {

    @Autowired
    private QueueService queueService;

    // Tích hợp HtmlSyntaxValidator
    private final HtmlSyntaxValidator htmlSyntaxValidator = new HtmlSyntaxValidator();

    /**
     * Phân loại các thẻ HTML chứa nội dung và đẩy vào queue sau khi kiểm tra cú pháp
     * (chỉ áp dụng với phương thức tải tệp HTML).
     *
     * @param rawHtml Nội dung HTML thô
     * @throws IllegalArgumentException nếu cú pháp HTML không hợp lệ
     */
    public void validateAndClassifyContent(String rawHtml) {
        System.out.println("Raw HTML received for validation: " + rawHtml); // Log nội dung HTML

        // Kiểm tra cú pháp HTML
        boolean isValid = htmlSyntaxValidator.validate(rawHtml);
        System.out.println("HTML syntax validation result: " + isValid); // Log kết quả kiểm tra cú pháp

        if (!isValid) {
            throw new IllegalArgumentException("HTML syntax error: unmatched tags");
        }

        // Nếu cú pháp hợp lệ, tiếp tục phân loại và đưa vào Queue
        classifyAndEnqueueContent(rawHtml);
    }

    /**
     * Phân loại các thẻ HTML chứa nội dung và đẩy vào queue.
     */
    public void classifyAndEnqueueContent(String rawHtml) {
        System.out.println("Classifying and enqueuing content."); // Log quá trình phân loại
        Document document = Jsoup.parse(rawHtml);
        Elements contentElements = document.select("title, p, h1, h2, h3, h4, h5, h6, div, span");

        for (Element element : contentElements) {
            HtmlData htmlData = new HtmlData(element.outerHtml());
            System.out.println("Enqueuing HTML element: " + htmlData.getRawHtml()); // Log nội dung từng phần tử
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

    /**
     * Phân loại nội dung HTML theo thẻ (dùng cho phương thức khác nếu cần).
     */
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
