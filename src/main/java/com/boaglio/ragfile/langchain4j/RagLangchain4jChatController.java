package com.boaglio.ragfile.langchain4j;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URISyntaxException;
import java.util.Map;

@RestController
@Tag(name = "RAG")
public class RagLangchain4jChatController {

    private final RagLangchain4jChatService chatService;

    public RagLangchain4jChatController(RagLangchain4jChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/ai/rag/langchain4j/generate")
    public Map generate(@RequestParam(value = "message", defaultValue = "Qual foi a causa da morte ?") String message) throws URISyntaxException {
        return Map.of("ollama", chatService.run(message));
    }

}