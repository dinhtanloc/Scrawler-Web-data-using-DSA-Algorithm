package ueh.service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ueh.util.Queue;

public class HtmlSyntaxValidator {

    private final Queue<String> htmlQueue; // Queue dùng để lưu các thẻ

    public HtmlSyntaxValidator() {
        this.htmlQueue = new Queue<>(); // Tận dụng Queue.java
    }

    /**
     * Phân tích cú pháp HTML và kiểm tra tính hợp lệ
     * 
     * @param rawHtml Nội dung HTML cần kiểm tra
     * @return true nếu cú pháp hợp lệ, false nếu không hợp lệ
     */
    public boolean validate(String rawHtml) {
        List<String> openTags = new ArrayList<>(); // Lưu thẻ mở

        // Danh sách các thẻ tự đóng phổ biến
        List<String> selfClosingTags = List.of("img", "br", "hr", "input", "link", "meta", "area", "base", "col", "embed", "param", "source", "track", "wbr");

        // Sử dụng Regex để tách các thẻ HTML
        Pattern pattern = Pattern.compile("<(/?\\w+)[^>]*>");
        Matcher matcher = pattern.matcher(rawHtml);

        // Thêm tất cả các thẻ vào Queue
        while (matcher.find()) {
            String tag = matcher.group(1); // Lấy tên thẻ (không bao gồm <, >)
            htmlQueue.enqueue(tag.trim()); // Thêm vào Queue
            System.out.println("Enqueued tag: " + tag.trim()); // Log thao tác enqueue
        }

        // Kiểm tra từng thẻ trong Queue
        while (!htmlQueue.isEmpty()) {
            String tag = htmlQueue.dequeue();
            System.out.println("Dequeued tag: " + tag); // Log thao tác dequeue
            System.out.println("Open tags before processing: " + openTags); // Log danh sách thẻ mở

            if (selfClosingTags.contains(tag)) {
                // Thẻ tự đóng
                System.out.println("Self-closing tag detected and processed: " + tag); // Log thẻ tự đóng
                continue;
            }

            if (!tag.startsWith("/")) {
                // Thẻ mở
                openTags.add(tag);
                System.out.println("Added to open tags: " + tag); // Log thêm thẻ mở
            } else {
                // Thẻ đóng
                if (openTags.isEmpty() || !openTags.get(openTags.size() - 1).equals(tag.substring(1))) {
                    System.out.println("Syntax error detected. Tag mismatch or unmatched closing tag: " + tag); // Log lỗi
                    return false; // Lỗi cú pháp
                }
                String matchedTag = openTags.remove(openTags.size() - 1); // Xóa thẻ mở đã khớp
                System.out.println("Matched closing tag: " + tag + " with opening tag: " + matchedTag); // Log khớp thẻ
            }

            System.out.println("Open tags after processing: " + openTags); // Log danh sách thẻ mở sau xử lý
        }

        // Nếu còn thẻ mở trong danh sách, báo lỗi
        if (!openTags.isEmpty()) {
            System.out.println("Syntax error detected. Unmatched opening tags remain: " + openTags); // Log lỗi thẻ mở
            return false;
        }

        System.out.println("Validation successful. All tags matched."); // Log thành công
        return true; // Không phát hiện lỗi
    }
}
