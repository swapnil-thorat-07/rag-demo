package com.example.embeddings;

import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.openai.OpenAiEmbeddingModel;
import dev.langchain4j.data.embedding.Embedding;

public class EmbeddingGenerator {

    private final EmbeddingModel model;

    public EmbeddingGenerator(String apiKey) {

        this.model = null;
       /*this.model = OpenAiEmbeddingModel.builder()
                .apiKey(apiKey)
                .build();*/
    }

    public Embedding generate(String text, boolean useCachedEmbeddings) {

        Embedding embedding = null;
        if(useCachedEmbeddings){
            // Use Cached Embeddings loaded from a file.
            embedding = EmbeddingCache
                    .getInstance()
                    .getEmbedding();
             /*
              //Use Cached Hardcoded Embeddings.
               Embedding embedding = CachedEmbeddingSingleton
                .getInstance()
                .getEmbedding();*/
        }
        else {
            //Use Embedding Generated from the actual Open AI Model
            embedding = model.embed(text).content();
        }

        return embedding;
    }
}