package com.boaglio.ragfile.langchain4j;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

import static com.boaglio.SpringOllama.*;
import static dev.langchain4j.data.document.loader.FileSystemDocumentLoader.loadDocuments;

@Service
public class RagLangchain4jChatService {

    private static Assistant getAssistant() throws URISyntaxException {

        var docPath = Paths.get(Objects.requireNonNull(RagLangchain4jChatService.class.getClassLoader().getResource(DOC)).toURI());

        List<Document> documents = loadDocuments(docPath.getParent());

        var ollamaModel = OllamaChatModel.builder()
                .baseUrl(OLLAMA_HOST)
                .modelName(MODEL)
                .build();

        return AiServices.builder(Assistant.class)
                .chatLanguageModel(ollamaModel)
                .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
                .contentRetriever(createContentRetriever(documents))
                .build();
    }

    public String run(String userPrompt) throws URISyntaxException {
        return getAssistant().answer(userPrompt);
    }

    private static ContentRetriever createContentRetriever(List<Document> documents) {

        InMemoryEmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();

        EmbeddingStoreIngestor.ingest(documents, embeddingStore);

        return EmbeddingStoreContentRetriever.from(embeddingStore);
    }

}