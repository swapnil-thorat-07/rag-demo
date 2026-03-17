package com.example.embeddings;
import dev.langchain4j.data.embedding.Embedding;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class EmbeddingCache {

    private final Embedding embedding;

    private EmbeddingCache() {
        float[] vector = loadVectorFromFile("/embedding.txt");
        this.embedding = Embedding.from(vector);
    }

    private float[] loadVectorFromFile(String resourcePath) {

        try {
            InputStream is = getClass().getResourceAsStream(resourcePath);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));

            List<Float> values = new ArrayList<>();

            String line;
            while ((line = reader.readLine()) != null) {
                values.add(Float.parseFloat(line.trim()));
            }

            float[] vector = new float[values.size()];
            for (int i = 0; i < values.size(); i++) {
                vector[i] = values.get(i);
            }

            return vector;

        } catch (Exception e) {
            throw new RuntimeException("Failed to load embedding file", e);
        }
    }

    private static class Holder {
        private static final EmbeddingCache INSTANCE = new EmbeddingCache();
    }

    public static EmbeddingCache getInstance() {
        return Holder.INSTANCE;
    }

    public Embedding getEmbedding() {
        return embedding;
    }
}