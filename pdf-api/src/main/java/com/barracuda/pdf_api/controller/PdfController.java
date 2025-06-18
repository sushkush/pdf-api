package com.barracuda.pdf_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.barracuda.pdf_api.model.PdfMetadata;
import com.barracuda.pdf_api.service.PdfService;

import java.io.InputStream;

@RestController
public class PdfController {

    @Autowired
    private PdfService pdfService;

    @PostMapping("/scan")
    public ResponseEntity<?> scanPdf(@RequestParam("file") MultipartFile file) {
        try {
            if (!file.getOriginalFilename().endsWith(".pdf")) {
                return ResponseEntity.badRequest().body("Invalid file type. Only PDFs allowed.");
            }

            byte[] fileBytes = file.getBytes();
            String hash = pdfService.calculateSha256(fileBytes);
            InputStream stream = file.getInputStream();

            pdfService.processMetadataAsync(hash, stream);
            return ResponseEntity.ok().body("{ \"sha256\": \"" + hash + "\" }");

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Something went wrong.");
        }
    }

    @GetMapping("/lookup/{hash}")
    public ResponseEntity<?> lookup(@PathVariable String hash) {
        PdfMetadata data = pdfService.getMetadata(hash);
        if (data == null) {
            return ResponseEntity.status(404).body("Record not found.");
        } else if (data.isProcessing()) {
            return ResponseEntity.ok("Metadata is still being processed...");
        } else {
            return ResponseEntity.ok(data);
        }
    }
}
