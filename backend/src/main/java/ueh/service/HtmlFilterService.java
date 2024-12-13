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
import java.util.Stack;
import ueh.util.Queue;


@Service
public class HtmlFilterService {

    @Autowired
    private QueueService queueService;

    private final Queue<String> htmlQueue= new Queue<>(); 


    public boolean validate(String rawHtml) {
        List<String> openTags = new ArrayList<>();
        List<String> selfClosingTags = List.of("img", "br", "hr", "input", "link", "meta", "a", "area", "base", "col", "embed", "param", "source", "track", "wbr");
        List<String> svgTags = List.of("svg", "circle", "rect", "path", "g", 
            "line", "polyline", "polygon", "ellipse", 
            "defs", "mask", "pattern", "text", "use",
            "image", "foreignObject", "clippath", "tspan",
            "a", "marker", "filter", "symbol"
        );

    
        Pattern pattern = Pattern.compile("<(/?\\w+)[^>]*>");
        Matcher matcher = pattern.matcher(rawHtml);
    
        while (matcher.find()) {
            String tag = matcher.group(1);
            if (svgTags.contains(tag)) {
                continue; 
            }
            htmlQueue.enqueue(tag.trim()); 
        }
    
        while (!htmlQueue.isEmpty()) {
            String tag = htmlQueue.dequeue();    
            if (selfClosingTags.contains(tag)) {
                continue;
            }
    
            if (!tag.startsWith("/")) {
                openTags.add(tag);
            } else {
                if (openTags.isEmpty() || !openTags.get(openTags.size() - 1).equals(tag.substring(1))) {
                    return false;
                }
                String matchedTag = openTags.remove(openTags.size() - 1);
            }
    
        }
    
        if (!openTags.isEmpty()) {
            return false;
        }
    
        return true;
    }
    


    public Map<String, Object> classifyContent(String rawHtml) {
        Map<String, Object> tagContentMap = new HashMap<>();
        Queue<String> openTags = new Queue<>();
        List<String> selfClosingTags = List.of("img", "br", "hr", "input", "link", "meta", "area", "base", "col", "embed", "param", "source", "track", "wbr");
        String[] tags = {"title", "p", "h1", "h2", "h3", "h4", "h5", "h6"};
    
        Pattern pattern = Pattern.compile("<(/?\\w+)[^>]*>([^<]*)");
        Matcher matcher = pattern.matcher(rawHtml);

        
    
        while (matcher.find()) {
            String tag = matcher.group(1).trim();
            String content = matcher.group(2).trim();
    
            openTags.enqueue(tag);
    
            if (!content.isEmpty() && List.of(tags).contains(tag)) {
                if (tagContentMap.containsKey(tag)) {
                    Object currentValue = tagContentMap.get(tag);
                    if (currentValue instanceof List) {
                        ((List<String>) currentValue).add(content);
                    } else if (currentValue instanceof String) {
                        List<String> contentList = new ArrayList<>();
                        contentList.add((String) currentValue);
                        contentList.add(content);
                        tagContentMap.put(tag, contentList);
                    }
                } else {
                    tagContentMap.put(tag, new ArrayList<>(List.of(content)));
                }
            }
        }
        for (String tag : List.of("h1", "h4")) {
            int index = 0;
            int startIndex = rawHtml.indexOf("<" + tag + ">", index);
            while (startIndex != -1) {
                int endIndex = rawHtml.indexOf("</" + tag + ">", startIndex);
                if (endIndex != -1) {
                    String content = rawHtml.substring(startIndex + tag.length() + 2, endIndex).trim();
    
                    if (!content.isEmpty() && !selfClosingTags.contains(tag)) {
                        if (tagContentMap.containsKey(tag)) {
                            Object currentValue = tagContentMap.get(tag);
                            if (currentValue instanceof List) {
                                ((List<String>) currentValue).add(content);
                            } else if (currentValue instanceof String) {
                                List<String> contentList = new ArrayList<>();
                                contentList.add((String) currentValue);
                                contentList.add(content);
                                tagContentMap.put(tag, contentList);
                            }
                        } else {
                            tagContentMap.put(tag, new ArrayList<>(List.of(content)));
                        }
                    }
                    index = endIndex + tag.length() + 3; 
                } else {
                    break;
                }
                startIndex = rawHtml.indexOf("<" + tag + ">", index);
            }
        }
    
        while (!openTags.isEmpty()) {
            String tag = openTags.dequeue();
            if (selfClosingTags.contains(tag)) {
                continue;
            }
        }
        System.out.println(tagContentMap);
        return tagContentMap;
    }
    

    public static Map<String, List<String>> extractContentWithoutTags(String html) {
        String regex = "<(/?\\w+)([^>]*?)>";
        Pattern tagPattern = Pattern.compile(regex);
        Matcher matcher = tagPattern.matcher(html);

        Stack<String> tagStack = new Stack<>();
        Queue<String> closingQueue = new Queue<>();
        Map<String, List<String>> contentMap = new HashMap<>();
        int lastIndex = 0;

        while (matcher.find()) {
            if (matcher.start() > lastIndex) {
                String content = html.substring(lastIndex, matcher.start()).trim();
                if (!content.isEmpty() && !tagStack.isEmpty()) {
                    String currentTag = tagStack.peek();
                    contentMap.computeIfAbsent(currentTag, k -> new ArrayList<>()).add(content);
                }
            }

            String tag = matcher.group(1).toLowerCase();

            if (matcher.group(2).endsWith("/")) {
                lastIndex = matcher.end();
                continue;
            }

            if (tag.equals("meta") || tag.equals("link") || tag.equals("a")) {
                lastIndex = matcher.end();
                continue;
            }

            if (!tag.startsWith("/")) {
                tagStack.push(tag);
            } else {
                closingQueue.enqueue(tag.substring(1));
                if (tagStack.isEmpty() || !tagStack.peek().equals(closingQueue.peek())) {
                    throw new IllegalArgumentException("Mismatched closing tag: " + tag);
                }
                closingQueue.dequeue();
                tagStack.pop();
            }

            lastIndex = matcher.end();
        }
        if (lastIndex < html.length()) {
            String content = html.substring(lastIndex).trim();
            if (!content.isEmpty() && !tagStack.isEmpty()) {
                String currentTag = tagStack.peek();
                contentMap.computeIfAbsent(currentTag, k -> new ArrayList<>()).add(content);
            }
        }
        if (!tagStack.isEmpty()) {
            throw new IllegalArgumentException("Unclosed tags remain: " + tagStack);
        }

        return contentMap;
    }
}
