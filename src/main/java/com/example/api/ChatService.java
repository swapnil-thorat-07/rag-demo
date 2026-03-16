package com.example.api;

import org.springframework.stereotype.Service;
import com.example.rag.RAGPipeline;
@Service
public class ChatService {

    private final RAGPipeline pipeline;

    public ChatService() {
        this.pipeline = new RAGPipeline();
    }

    public String ask(String question) {

        return pipeline.chat(question);
    }
}