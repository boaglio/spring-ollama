package com.boaglio;


import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringOllama {

    public static final String OLLAMA_HOST = "http://localhost:11434";
    public static final String MODEL = "llama3.2";
    public static final String DOC =  "memoriasBras-cap1.pdf";

    public static void main(String[] args) {
        SpringApplication.run(SpringOllama.class, args);
    }

    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("DevMultitask Spring Ollama API")
                        .description("Exemplos de Spring Ollama - por Fernando Boaglio - YouTube @DevMultitask ")
                        .version("v1.0"))
                .externalDocs(new ExternalDocumentation().description("GitHub").url("https://github.com/boaglio/spring-ollama"));
    }

}