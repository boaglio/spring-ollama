package com.boaglio.ragdb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;

@Tag(name = "RAG")
@RestController
public class RagFileDbController {

    private final ChatClient chatClient;

    private static final Logger log = LoggerFactory.getLogger(RagFileDbController.class);

    @Value("classpath:/rag-prompt-template.st")
    private Resource ragPromptTemplate;

    private final RagConfiguration ragConfiguration;

    public RagFileDbController(ChatClient.Builder builder, RagConfiguration ragConfiguration) {
        this.ragConfiguration = ragConfiguration;
        this.chatClient = builder.build();
    }

    @GetMapping("/ai/rag/filedb/generate")
    public String generate(@RequestParam(value = "message", defaultValue = "Qual o código do status do Conflict ?") String message) throws IOException {

        List<Document> similarDocuments = ragConfiguration.getSimpleVectorStore().similaritySearch(SearchRequest.query(message).withTopK(2));
        List<String> contentList = similarDocuments.stream().map(Document::getContent).toList();
        PromptTemplate promptTemplate = new PromptTemplate(ragPromptTemplate.getContentAsString(StandardCharsets.UTF_8));
        var promptParameters = new HashMap<String,Object>();
        promptParameters.put("input",message);
        promptParameters.put("documents",String.join("\n",contentList));
        var prompt = promptTemplate.create(promptParameters);

        log.info(prompt.getContents());

        return chatClient.prompt(prompt).call().content();
    }

}