package com.barracuda.pdf_api.service;



import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.springframework.stereotype.Service;

import com.barracuda.pdf_api.model.PdfMetadata;

import java.io.InputStream;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PdfService {

    private Map<String, PdfMetadata> metadataStore = new ConcurrentHashMap<>();

    public String calculateSha256(byte[] fileBytes) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = digest.digest(fileBytes);
        StringBuilder hexString = new StringBuilder();
        for (byte b : hashBytes) {
            String hex = String.format("%02x", b);
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public void processMetadataAsync(String hash, InputStream inputStream) {

       Thread t1= new Thread(() -> {
            try {
                PDDocument doc = PDDocument.load(inputStream);
                PDDocumentInformation info = doc.getDocumentInformation();

                PdfMetadata data = new PdfMetadata();
                data.setSha256(hash);
                data.setVersion(doc.getVersion() + "");
                data.setProducer(info.getProducer());
                data.setAuthor(info.getAuthor());
                if (info.getCreationDate() != null) {
                    data.setCreatedDate(info.getCreationDate().toInstant().toString());
                }
                if (info.getModificationDate() != null) {
                    data.setModifiedDate(info.getModificationDate().toInstant().toString());
                }
                data.setSubmittedDate(Instant.now().toString());
               
                metadataStore.put(hash, data);
                doc.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        t1.start();

        PdfMetadata pdfMetadata = new PdfMetadata();
        pdfMetadata.setSha256(hash);
        metadataStore.put(hash, pdfMetadata);
    }

    public PdfMetadata getMetadata(String hash) {
        return metadataStore.get(hash);
    }
}

