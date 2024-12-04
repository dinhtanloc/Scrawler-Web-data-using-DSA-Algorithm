package ueh.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

import java.util.Map;
import ueh.model.HtmlData;
import ueh.service.HtmlCrawlerService;
import ueh.service.HtmlFilterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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
    @PostMapping("/crawl")
    public String crawlHtml(@RequestBody Map<String, String> body) throws IOException {
        String url = body.get("url");
        System.out.println(url);
        HtmlData htmlData = crawlerService.crawl(url);
        return htmlData.getRawHtml();
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

    @PostMapping("/read")
    public ResponseEntity<Map<String, Object>> readHtml(@RequestBody Map<String, String> body) {
        String htmlContent = body.get("htmlContent");
        if (htmlContent == null || htmlContent.isEmpty()) {
            throw new IllegalArgumentException("Missing input: 'htmlContent' must be provided");
        }
    
        Map<String, Object> contentMap = filterService.classifyContent(htmlContent);
        return ResponseEntity.ok(contentMap);
    }
}
