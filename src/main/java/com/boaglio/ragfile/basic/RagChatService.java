package com.boaglio.ragfile.basic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class RagChatService {

    private final ChatClient.Builder chatClientBuilder;
    private final PdfService pdfService;

    Logger log = LoggerFactory.getLogger(RagChatService.class);

    public RagChatService(ChatClient.Builder chatClientBuilder, PdfService pdfService) {
        this.chatClientBuilder = chatClientBuilder;
        this.pdfService = pdfService;
    }

    public String run(String userPrompt) {

        var chatClient = chatClientBuilder.build();
        var smallContext = pdfService.getPdfContent();

        var prompt = "Context: ["+smallContext+"] . User prompt: " + userPrompt;

        log.info(prompt);

        return chatClient
                .prompt()
                .user(prompt)
                .call()
                .content()
                .replace("\n", "");
    }

}