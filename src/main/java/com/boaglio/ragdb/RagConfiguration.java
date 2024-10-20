package com.boaglio.ragdb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;

@Configuration
public class RagConfiguration {
    
    private static final Logger log = LoggerFactory.getLogger(RagConfiguration.class);

    @Value("vectorstore.json")
    private String vectorStoreName;

    @Value("classpath:/rfc7231.txt")
    private Resource documentFromTXT;

    private final EmbeddingModel embeddingModel;

    public RagConfiguration(EmbeddingModel embeddingModel) {
        this.embeddingModel = embeddingModel;
    }

    public SimpleVectorStore getSimpleVectorStore() {

        var simpleVectorStore = new SimpleVectorStore(embeddingModel);
        var vectorStoreFile = getVectorStoreFile();
        if (vectorStoreFile.exists()) {

            log.info("Usando arquivo do Vector Store");
            simpleVectorStore.load(vectorStoreFile);

        } else {

            long startTime = System.currentTimeMillis();

            log.info("Criando Vector Store, carregando...");
            var textReader = new TextReader(documentFromTXT);
            textReader.getCustomMetadata().put("filename", "rfc7231.txt");

            List<Document> documents = textReader.get();
            log.info("Total de AI Documents: %d".formatted(documents.size()));

            var textSplitter = new TokenTextSplitter();
            List<Document> splitDocuments = textSplitter.apply(documents);
            simpleVectorStore.add(splitDocuments);

            log.info("Total de Tokens: %d".formatted(splitDocuments.size()));
            simpleVectorStore.save(vectorStoreFile);

            long endTime = System.currentTimeMillis();
            long executionTime = endTime - startTime;
            log.info("Tempo de execução : " + executionTime + " ms");

        }
        return simpleVectorStore;
    }

    private File getVectorStoreFile() {

        var path = Paths.get("src", "main", "resources");
        var absolutePath = path.toFile().getAbsolutePath() + "/" + vectorStoreName;
        return new File(absolutePath);

    }

}