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

    private final HtmlSyntaxValidator htmlSyntaxValidator = new HtmlSyntaxValidator();

    /**
     * Phân loại các thẻ HTML, đẩy vào queue và kiểm tra cú pháp.
     *
     * @param rawHtml Nội dung HTML thô
     * @throws IllegalArgumentException nếu cú pháp HTML không hợp lệ
     */
    public void validateAndClassifyContent(String rawHtml) {
        System.out.println("Raw HTML received for processing: " + rawHtml); // Log nội dung HTML

        // Phân loại nội dung HTML theo thẻ
        Map<String, Object> tagContentMap = classifyContent(rawHtml);

        // Kiểm tra cú pháp từng thẻ trong danh sách đã phân loại
        boolean isValid = validateAllTags(tagContentMap);

        if (isValid) {
            System.out.println("All tags are valid. Enqueuing content.");
            classifyAndEnqueueContent(rawHtml);
        } else {
            System.err.println("HTML syntax error detected. Processing stopped.");
            throw new IllegalArgumentException("HTML syntax error detected. See logs for details.");
        }
    }

    /**
     * Kiểm tra cú pháp của tất cả các thẻ HTML trong danh sách đã phân loại.
     *
     * @param tagContentMap Bản đồ chứa các thẻ và nội dung
     * @return true nếu tất cả các thẻ hợp lệ, false nếu có lỗi
     */
    private boolean validateAllTags(Map<String, Object> tagContentMap) {
        boolean isValid = true;

        for (Map.Entry<String, Object> entry : tagContentMap.entrySet()) {
            String tag = entry.getKey();
            Object content = entry.getValue();

            // Duyệt từng nội dung của thẻ
            if (content instanceof List) {
                @SuppressWarnings("unchecked")
                List<String> contentList = (List<String>) content;
                for (String item : contentList) {
                    if (!validateTagSyntax(tag, item)) {
                        isValid = false;
                    }
                }
            } else {
                if (!validateTagSyntax(tag, content.toString())) {
                    isValid = false;
                }
            }
        }

        return isValid;
    }

    /**
     * Kiểm tra cú pháp của một thẻ HTML.
     *
     * @param tag Tên thẻ
     * @param content Nội dung thẻ
     * @return true nếu cú pháp hợp lệ, false nếu không
     */
    private boolean validateTagSyntax(String tag, String content) {
        String wrappedTag = "<" + tag + ">" + content + "</" + tag + ">";
        boolean isValid = htmlSyntaxValidator.validate(wrappedTag);

        System.out.println("Validating tag: " + wrappedTag + " -> " + isValid); // Log kết quả kiểm tra

        if (!isValid) {
            System.err.println("Syntax error detected in tag: " + wrappedTag);
        }

        return isValid;
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
