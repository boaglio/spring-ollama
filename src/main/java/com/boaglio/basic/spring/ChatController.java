package com.boaglio.basic.spring;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Tag(name = "Basic")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/ai/basic/basic/generate")
    public Map generate(@RequestParam(value = "message", defaultValue = "Mostre uma frase de alguém famoso") String message) {
        return Map.of("ollama", chatService.run(message));
    }

}