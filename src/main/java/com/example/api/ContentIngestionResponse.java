package com.example.api;

public class ContentIngestionResponse {

    private String status;
    private int chunksStored;

    public ContentIngestionResponse() {}

    public ContentIngestionResponse(String status, int chunksStored) {
        this.status = status;
        this.chunksStored = chunksStored;
    }

    public String getStatus() {
        return status;
    }

    public int getChunksStored() {
        return chunksStored;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setChunksStored(int chunksStored) {
        this.chunksStored = chunksStored;
    }
}