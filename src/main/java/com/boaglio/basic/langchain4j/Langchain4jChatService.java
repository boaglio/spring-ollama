package com.boaglio.basic.langchain4j;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.ollama.OllamaChatModel;

import org.springframework.stereotype.Service;

import static com.boaglio.SpringOllama.MODEL;
import static com.boaglio.SpringOllama.OLLAMA_HOST;

@Service
public class Langchain4jChatService {

    private static ChatLanguageModel connectModel() {
        return OllamaChatModel.builder()
                .baseUrl(OLLAMA_HOST)
                .modelName(MODEL)
                .build();
    }

    public String run(String userPrompt) {
        var model = connectModel();
        return  model.generate(userPrompt);
    }

}