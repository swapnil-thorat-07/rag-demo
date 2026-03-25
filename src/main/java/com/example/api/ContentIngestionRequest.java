package com.example.api;

public class ContentIngestionRequest {

    private String textContent;

    public ContentIngestionRequest() {}

    public ContentIngestionRequest(String textContent) {
        this.textContent = textContent;
    }

    public String getTextContent() {
        return textContent;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }
}