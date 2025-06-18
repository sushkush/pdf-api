package com.barracuda.pdf_api.model;


public class PdfMetadata {
    private String sha256;
    private String version;
    private String producer;
    private String author;
    private String createdDate;
    private String modifiedDate;
    private String submittedAt;
    

    // Getters and setters 
    
    public String getSha256() { 
        return sha256; 
    }

    public void setSha256(String sha256) {
         this.sha256 = sha256; 
    }

    public String getVersion() { 
        return version;
     }

    public void setVersion(String version) {
         this.version = version; 
    }

    public String getProducer() {
         return producer;
    }
    public void setProducer(String producer) {
         this.producer = producer;
    }

    public String getAuthor() {
         return author; 
    }
    public void setAuthor(String author) {
         this.author = author; 
    }

    public String getCreatedDate() {
         return createdDate; 
    }
    public void setCreatedDate(String createdDate) {
         this.createdDate = createdDate; 
    }

    public String getModifiedDate() {
         return modifiedDate; 
    }
    public void setModifiedDate(String modifiedDate) {
         this.modifiedDate = modifiedDate; 
    }

    public String getSubmittedDate() {
         return submittedAt; 
    }
    public void setSubmittedDate(String submittedAt) {
         this.submittedAt = submittedAt; 
    }

}


