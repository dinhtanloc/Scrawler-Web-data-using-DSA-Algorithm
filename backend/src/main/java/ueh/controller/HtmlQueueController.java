package ueh.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ueh.model.HtmlData;
import ueh.service.HtmlCrawlerService;
import ueh.service.HtmlFilterService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/html")
public class HtmlQueueController {
    @Autowired
    private HtmlCrawlerService crawlerService;

    @Autowired
    private HtmlFilterService filterService;

    @CrossOrigin(origins = "*")
    @PostMapping(value = "/crawl", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> crawlHtml(@RequestBody Map<String, String> body) {
        String url = body.get("url");
        try {
            HtmlData htmlData = crawlerService.crawl(url);
            String rawHtml = htmlData.getRawHtml();
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


    @PostMapping(value="/read", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> readHtml(@RequestBody Map<String, String> body) {
        String htmlContent = body.get("htmlContent");
        boolean isUrlsearch = Boolean.parseBoolean(body.get("urlCheck"));
        if (htmlContent == null || htmlContent.isEmpty()) {
            throw new IllegalArgumentException("Missing input: 'htmlContent' must be provided");
        }

        if (!isUrlsearch) {
            
            boolean isValidHtml = filterService.validate(htmlContent);
            if (!isValidHtml) {
                Map<String, Object> errorResponse = Map.of(
                    "error", "Invalid HTML content",
                    "status", 400
                );
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            }
        }

        Map<String, Object> contentMap = filterService.classifyContent(htmlContent);
        return ResponseEntity.ok(contentMap);
    }
}