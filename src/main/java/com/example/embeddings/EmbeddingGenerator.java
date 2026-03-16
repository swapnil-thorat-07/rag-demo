package com.example.embeddings;

import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.openai.OpenAiEmbeddingModel;
import dev.langchain4j.data.embedding.Embedding;

public class EmbeddingGenerator {

    private final EmbeddingModel model;

    public EmbeddingGenerator(String apiKey) {

        this.model = OpenAiEmbeddingModel.builder()
                .apiKey(apiKey)
                .build();
    }

    public Embedding generate(String text) {

        return model.embed(text).content();
    }
}