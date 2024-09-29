package com.boaglio.ragfile.basic;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Tag(name = "RAG")
public class RagChatController {

    private final RagChatService chatService;

    public RagChatController(RagChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/ai/rag/basic/generate")
    public Map generate(@RequestParam(value = "message", defaultValue = "Qual foi a causa da morte ?") String message) {
        return Map.of("ollama", chatService.run(message));
    }

}