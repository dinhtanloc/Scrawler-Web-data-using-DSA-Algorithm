package ueh.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ueh.service.ChunkService;
import ueh.model.Chunk;

@RestController
@RequestMapping("/chunks")
public class TextController {

    @Autowired
    private ChunkService chunkService;

    /**
     * Lưu một chunk vào MongoDB.
     * @param chunk Chunk cần lưu.
     * @return Chunk đã lưu.
     */
    @PostMapping
    public Chunk saveChunk(@RequestBody Chunk chunk) {
        return chunkService.saveChunk(chunk);
    }

    /**
     * Xử lý nội dung HTML, tách thành các chunk và lưu vào MongoDB.
     * @param htmlContent Nội dung HTML cần xử lý.
     * @return Thông báo trạng thái.
     */
    @PostMapping("/save")
    public String saveHtml(@RequestBody String htmlContent) {
        try {
            chunkService.embededHTML(htmlContent);
            return "HTML content processed and saved as chunks.";
        } catch (Exception e) {
            return "Error processing HTML content: " + e.getMessage();
        }
    }
}
