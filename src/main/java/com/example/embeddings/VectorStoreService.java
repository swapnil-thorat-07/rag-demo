package com.example.embeddings;

import dev.langchain4j.data.embedding.Embedding;
import java.util.ArrayList;
public class VectorStoreService {

    private final QdrantClient client;

    public VectorStoreService() {
        this.client = new QdrantClient();
    }

    public void store(String id, Embedding embedding, String payload) {

      /*  client.insertVector(
                id,
                new ArrayList<Float>().add(embedding.vector()),
                payload
        );*/
    }
}