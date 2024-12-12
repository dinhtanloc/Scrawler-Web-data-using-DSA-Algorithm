package ueh.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import ueh.model.HtmlData;
import ueh.service.HtmlCrawlerService;
import ueh.service.HtmlFilterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import java.io.IOException;

// @CrossOrigin(origins = "http://localhost:3000")
@CrossOrigin(origins = "*")
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
        // System.out.println("Crawling URL: " + url);

        try {
            HtmlData htmlData = crawlerService.crawl(url);
            String rawHtml = htmlData.getRawHtml();
            // System.out.println("HTML Content: " + rawHtml);

            Map<String, Object> response = Map.of(
                "html", rawHtml,
                "status", 200,
                "message", "Crawl successful"
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {
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

    @PostMapping(value="/read", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> readHtml(@RequestBody Map<String, String> body) {
        String htmlContent = body.get("htmlContent");
        if (htmlContent == null || htmlContent.isEmpty()) {
            throw new IllegalArgumentException("Missing input: 'htmlContent' must be provided");
        }

        // boolean isValidHtml = filterService.validate(htmlContent);
        // System.out.println(isValidHtml);
    
        // if (!isValidHtml) {
        //     Map<String, Object> errorResponse = Map.of(
        //         "error", "Invalid HTML content",
        //         "status", 400
        //     );
        //     return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        // }

        Map<String, Object> contentMap = filterService.classifyContent(htmlContent);
        return ResponseEntity.ok(contentMap);
    }
}
