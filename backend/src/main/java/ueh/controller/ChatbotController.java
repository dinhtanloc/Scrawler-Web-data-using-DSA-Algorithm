package ueh.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.CrossOrigin;
import ueh.service.ChatBotService;
@CrossOrigin(origins = "*")
@RestController
public class ChatbotController {
    @Autowired
    private ChatBotService chatBotService;

    // @GetMapping("/chat")
    // public Map chat(@RequestParam(name = "query") String query) {
    //     return Map.of("answer", chatBotService.chat(query));
    // }

    @PostMapping("/chat")
    public Map<String, String> postChat(@RequestBody Map<String, String> request) {
        String query = request.get("query");
        String answer = chatBotService.chat(query);
        return Map.of("answer", answer);
    }
}