package com.boaglio.basic.langchain4j;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Tag(name = "Basic")
public class Langchain4jChatController {

    private final Langchain4jChatService chatService;

    public Langchain4jChatController(Langchain4jChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/ai/basic/langchain4j/generate")
    public Map generate(@RequestParam(value = "message", defaultValue = "Mostre uma frase de alguém famoso") String message) {
        return Map.of("ollama", chatService.run(message));
    }

}