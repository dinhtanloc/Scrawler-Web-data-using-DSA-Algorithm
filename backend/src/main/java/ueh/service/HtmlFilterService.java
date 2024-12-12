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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ueh.util.Queue;


@Service
public class HtmlFilterService {

    @Autowired
    private QueueService queueService;

    private final Queue<String> htmlQueue= new Queue<>(); 

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


    public boolean validate(String rawHtml) {
        List<String> openTags = new ArrayList<>(); 

        List<String> selfClosingTags = List.of("img", "br", "hr", "input", "link", "meta", "area", "base", "col", "embed", "param", "source", "track", "wbr");

        Pattern pattern = Pattern.compile("<(/?\\w+)[^>]*>");
        Matcher matcher = pattern.matcher(rawHtml);

        while (matcher.find()) {
            String tag = matcher.group(1); 
            htmlQueue.enqueue(tag.trim());
            System.out.println("Enqueued tag: " + tag.trim()); 
        }

        while (!htmlQueue.isEmpty()) {
            String tag = htmlQueue.dequeue();
            System.out.println("Dequeued tag: " + tag); 
            System.out.println("Open tags before processing: " + openTags); 

            if (selfClosingTags.contains(tag)) {
                System.out.println("Self-closing tag detected and processed: " + tag); 
                continue;
            }

            if (!tag.startsWith("/")) {
                openTags.add(tag);
                System.out.println("Added to open tags: " + tag); 
            } else {
                if (openTags.isEmpty() || !openTags.get(openTags.size() - 1).equals(tag.substring(1))) {
                    System.out.println("Syntax error detected. Tag mismatch or unmatched closing tag: " + tag); 
                    return false; 
                }
                String matchedTag = openTags.remove(openTags.size() - 1); 
                System.out.println("Matched closing tag: " + tag + " with opening tag: " + matchedTag);
            }

            System.out.println("Open tags after processing: " + openTags); 
        }

        if (!openTags.isEmpty()) {
            System.out.println("Syntax error detected. Unmatched opening tags remain: " + openTags); 
            return false;
        }

        System.out.println("Validation successful. All tags matched."); 
        return true;
    }

   


    public Map<String, Object> classifyContent(String rawHtml) {
        Map<String, Object> tagContentMap = new HashMap<>();
        Document document = Jsoup.parse(rawHtml);

        String[] tags = {"title", "p", "h1", "h2", "h3", "h4", "h5", "h6"};

        for (String tag : tags) {
            Elements elements = document.select(tag);
            // System.out.println(elements);
            validate(elements.toString());

            for (Element element : elements) {
                String content = element.text().trim();
                // System.out.println("Tag: " + tag + ", Content: " + content);

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
