package com.example.embeddings;

import java.util.List;

public class QdrantClient {

    public void insertVector(String id, List<Float> vector, String payload) {

        System.out.println("Insert vector ID: " + id);
        System.out.println("Vector size: " + vector.size());
        System.out.println("Payload: " + payload);
    }
}