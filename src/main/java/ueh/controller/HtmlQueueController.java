package ueh.controller;
import ueh.model.HtmlData;
import ueh.service.HtmlCrawlerService;
import ueh.service.HtmlFilterService;
import ueh.service.QueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/html")
public class HtmlQueueController {
    @Autowired
    private HtmlCrawlerService crawlerService;

    @Autowired
    private HtmlFilterService filterService;

    @Autowired
    private QueueService queueService;

    @PostMapping("/crawl")
    public String crawlAndEnqueue(@RequestParam String url) throws IOException, InterruptedException {
        HtmlData htmlData = crawlerService.crawl(url);
        queueService.enqueue(htmlData);
        return "HTML crawled and added to queue";
    }

    @GetMapping("/process")
    public HtmlData processHtmlFromQueue() throws InterruptedException {
        HtmlData htmlData = queueService.dequeue();
        String filteredHtml = filterService.filterHtml(htmlData.getRawHtml());
        htmlData.setFilteredHtml(filteredHtml);
        return htmlData;
    }
}

