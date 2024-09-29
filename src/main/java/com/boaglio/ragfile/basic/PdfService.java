package com.boaglio.ragfile.basic;

import jakarta.annotation.PostConstruct;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

import static com.boaglio.SpringOllama.DOC;

@Service
public class PdfService {

    private Logger log = LoggerFactory.getLogger(PdfService.class);
    private String pdfContent;

    @PostConstruct
    public void init() {
        try (InputStream is = getClass().getResourceAsStream("/"+DOC);
            var document = PDDocument.load(is)) {
            var pdfStripper = new PDFTextStripper();
            pdfContent = pdfStripper.getText(document);
            log.info("PDF carregado com sucesso: %d caracteres.".formatted(pdfContent.length()));
        } catch (IOException e) {
            log.error("Erro ao carregar o PDF: ",e);
        }
    }

    public String getPdfContent() {
        return pdfContent;
    }

}