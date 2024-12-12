package ueh.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ueh.service.ChunkService;
import ueh.model.Chunk;
import org.json.JSONObject;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/chunks")
public class TextController {

    @Autowired
    private ChunkService chunkService;

    @CrossOrigin(origins = "*")
    @PostMapping("/save")
    public String saveHtml(@RequestBody String htmlContent) {
        try {
            JSONObject jsonObject = new JSONObject(htmlContent);
            String processedContent = jsonObject.getString("processedContent");
            chunkService.embededHTML(processedContent);
            return "HTML content processed and saved as chunks.";
        } catch (Exception e) {
            return "Error processing HTML content: " + e.getMessage();
        }
    }
}
