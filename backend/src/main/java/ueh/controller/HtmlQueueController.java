package ueh.controller;

import ueh.model.HtmlData;
import ueh.service.HtmlCrawlerService;
import ueh.service.HtmlFilterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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
    @PostMapping("/crawl")
    public String crawlHtml(@RequestParam String url) throws IOException {
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
    @PostMapping("/read")
    public String readHtml(
            @RequestParam(required = false) String htmlContent,
            @RequestParam(required = false) MultipartFile file
    ) throws IOException {
        String rawHtml;

        // Kiểm tra xem dữ liệu đầu vào là htmlContent hay file
        if (htmlContent != null && !htmlContent.isEmpty()) {
            rawHtml = htmlContent;
        } else if (file != null && !file.isEmpty()) {
            rawHtml = new String(file.getBytes());
        } else {
            throw new IllegalArgumentException("Missing input: Either 'htmlContent' or 'file' must be provided");
        }

        // Xử lý nội dung HTML
        return filterService.filterHtml(rawHtml);
    }
}
