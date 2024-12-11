package ueh.controller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import ueh.model.HtmlData;
import ueh.service.HtmlCrawlerService;
import ueh.service.HtmlFilterService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/html")
public class HtmlQueueController {
    @Autowired
    private HtmlCrawlerService crawlerService;

    @Autowired
    private HtmlFilterService filterService;

    /**
     * Crawl HTML từ URL
     * @param url URL cần crawl
     * @return raw HTML
     * @throws IOException
     */
    @CrossOrigin(origins = "*")
    @PostMapping(value = "/crawl", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> crawlHtml(@RequestBody Map<String, String> body) {
        String url = body.get("url");
        System.out.println("Crawling URL: " + url);

        try {
            HtmlData htmlData = crawlerService.crawl(url); // Gọi service để lấy HTML
            String rawHtml = htmlData.getRawHtml();
            System.out.println("HTML Content: " + rawHtml);

            // Trả về HTML dưới dạng JSON
            Map<String, Object> response = Map.of(
                "html", rawHtml,
                "status", 200,
                "message", "Crawl successful"
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            // Xử lý lỗi và trả về phản hồi lỗi
            Map<String, Object> errorResponse = Map.of(
                "error", "Failed to crawl URL",
                "details", e.getMessage(),
                "status", 500
            );

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Đọc nội dung từ HTML
     * @param htmlContent Nội dung HTML trực tiếp (tuỳ chọn)
     * @param file File HTML tải lên (tuỳ chọn)
     * @return Nội dung đã xử lý
     * @throws IOException
     */
    // @PostMapping("/read")
    // public String readHtml(@RequestBody Map<String, String> body) throws IOException {
    //     String htmlContent = body.get("htmlContent");
    //     if (htmlContent == null || htmlContent.isEmpty()) {
    //         throw new IllegalArgumentException("Missing input: 'htmlContent' must be provided");
    //     }

    //     filterService.classifyAndEnqueueContent(htmlContent);
    //     return filterService.processQueue();
    // }

    @PostMapping(value="/read", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> readHtml(@RequestBody Map<String, String> body) {
        String htmlContent = body.get("htmlContent");
        System.out.println(htmlContent);
        if (htmlContent == null || htmlContent.isEmpty()) {
            throw new IllegalArgumentException("Missing input: 'htmlContent' must be provided");
        }
    
        Map<String, Object> contentMap = filterService.classifyContent(htmlContent);
        System.out.println(contentMap); 
        return ResponseEntity.ok(contentMap);
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
public ResponseEntity<String> uploadHtmlFile(@RequestParam("file") MultipartFile file) {
    try {
        // Đọc nội dung file
        String htmlContent = new String(file.getBytes(), StandardCharsets.UTF_8);
        System.out.println("HTML content received:\n" + htmlContent); // Log nội dung HTML

        // Kiểm tra cú pháp và xử lý
        filterService.validateAndClassifyContent(htmlContent);

        return ResponseEntity.ok("HTML content is valid and has been processed.");
    } catch (IllegalArgumentException e) {
        System.out.println("Validation error: " + e.getMessage());
        return ResponseEntity.badRequest().body("HTML syntax error: " + e.getMessage());
    } catch (IOException e) {
        System.out.println("File processing error: " + e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to process file: " + e.getMessage());
    }
}




}
