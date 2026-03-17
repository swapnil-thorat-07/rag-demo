package com.example.search;
import dev.langchain4j.data.embedding.Embedding;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * Singleton for in-memory vector search.
 * Supports 1536-dim embeddings and cosine similarity search.
 */
public class VectorSearchSingleton {

    private static VectorSearchSingleton instance;

    // Map of id -> embedding
    private final Map<String, float[]> vectorMap = new HashMap<>();

    private VectorSearchSingleton() {
        // private constructor
    }

    public static synchronized VectorSearchSingleton getInstance() {
        if (instance == null) {
            instance = new VectorSearchSingleton();
        }
        return instance;
    }

    /**
     * Load embeddings from a text file.
     * Each line: id comma-separated vector values
     */
    public void loadFromFile(Path filePath) throws IOException {
        try (BufferedReader br = Files.newBufferedReader(filePath)) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", 2);
                if (parts.length < 2) continue;

                String id = parts[0].trim();
                String[] vectorStr = parts[1].trim().split(",");
                float[] vector = new float[vectorStr.length];
                for (int i = 0; i < vectorStr.length; i++) {
                    vector[i] = Float.parseFloat(vectorStr[i]);
                }
                vectorMap.put(id, vector);
            }
        }
    }

    /**
     * Add a single embedding manually
     */
    public void addEmbedding(String id, Embedding embedding) {
        vectorMap.put(id, embedding.vector());
    }

    /**
     * Search top-K most similar vectors using cosine similarity
     */
    public List<SearchResult> search(float[] queryVector, int topK) {
        PriorityQueue<SearchResult> pq = new PriorityQueue<>(Comparator.comparingDouble(r -> r.score));

        for (Map.Entry<String, float[]> entry : vectorMap.entrySet()) {
            double score = cosineSimilarity(queryVector, entry.getValue());

            if (pq.size() < topK) {
                pq.add(new SearchResult(entry.getKey(), score));
            } else if (score > Objects.requireNonNull(pq.peek()).score) {
                pq.poll();
                pq.add(new SearchResult(entry.getKey(), score));
            }
        }

        List<SearchResult> results = new ArrayList<>(pq);
        results.sort((a, b) -> Double.compare(b.score, a.score)); // descending
        return results;
    }

    /**
     * Cosine similarity between two vectors
     */
    private double cosineSimilarity(float[] v1, float[] v2) {
        double dot = 0.0;
        double norm1 = 0.0;
        double norm2 = 0.0;

        for (int i = 0; i < v1.length; i++) {
            dot += v1[i] * v2[i];
            norm1 += v1[i] * v1[i];
            norm2 += v2[i] * v2[i];
        }
        return dot / (Math.sqrt(norm1) * Math.sqrt(norm2) + 1e-10);
    }

    /**
     * Search result holder
     */
    public static class SearchResult {
        public final String id;
        public final double score;

        public SearchResult(String id, double score) {
            this.id = id;
            this.score = score;
        }

        @Override
        public String toString() {
            return "SearchResult{id='" + id + "', score=" + score + "}";
        }
    }
}