package com.example.embeddings;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
public class PineconeStore {

    private final String apiKey;
    private final String indexHost;

    public PineconeStore(String apiKey, String indexHost) {
        this.apiKey = apiKey;
        this.indexHost = indexHost;
    }

    /**
     * Stores text + vector into Pinecone
     */
    public void store(String text, float[] vector) throws Exception {

        String id = UUID.randomUUID().toString(); // unique ID

        URL url = new URL(indexHost + "/vectors/upsert");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Api-Key", apiKey);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        // Build vector JSON
        JSONObject vectorObj = new JSONObject();
        vectorObj.put("id", id);

        JSONArray valuesArray = new JSONArray();
        for (float v : vector) {
            valuesArray.put(v);
        }
        vectorObj.put("values", valuesArray);

        // Store text as metadata
        JSONObject metadata = new JSONObject();
        metadata.put("text", text);

        vectorObj.put("metadata", metadata);

        JSONArray vectors = new JSONArray();
        vectors.put(vectorObj);

        JSONObject payload = new JSONObject();
        payload.put("vectors", vectors);

        // Send request
        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = payload.toString().getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        int responseCode = conn.getResponseCode();

        BufferedReader reader;
        if (responseCode >= 200 && responseCode < 300) {
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            reader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }

        String line;
        StringBuilder response = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();
        System.out.println("Response: " + response.toString());
    }

    // =========================
    // QUERY RAW JSON RESPONSE
    // =========================
    public String query(float[] queryVector, int topK) throws Exception {

        URL url = new URL(indexHost + "/query");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Api-Key", apiKey);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        String vectorArray = floatArrayToJson(queryVector);

        String payload = "{ " +
                "\"vector\": " + vectorArray + ", " +
                "\"topK\": " + topK + ", " +
                "\"includeMetadata\": true, " +
                "\"namespace\": \"default\" " +
                "}";

        return sendRequest(conn, payload);
    }

    // =========================
    // QUERY + EXTRACT TEXTS
    // =========================
    public List<String> queryAndExtractTexts(float[] queryVector, int topK) throws Exception {

        String response = query(queryVector, topK);

        List<String> results = new ArrayList<>();

        // VERY simple parsing (works because we know structure)
        String[] parts = response.split("\"text\":\"");

        for (int i = 1; i < parts.length; i++) {
            String text = parts[i].split("\"")[0];
            results.add(text);
        }

        return results;
    }

    // =========================
    // HELPER: SEND REQUEST
    // =========================
    private String sendRequest(HttpURLConnection conn, String payload) throws Exception {

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = payload.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        int responseCode = conn.getResponseCode();

        BufferedReader reader;
        if (responseCode >= 200 && responseCode < 300) {
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            reader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }

        StringBuilder response = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            response.append(line);
        }

        reader.close();

        if (responseCode >= 200 && responseCode < 300) {
            return response.toString();
        } else {
            throw new RuntimeException("Error: " + responseCode + " -> " + response);
        }
    }

    // =========================
    // HELPER: FLOAT ARRAY → JSON
    // =========================
    private String floatArrayToJson(float[] vector) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < vector.length; i++) {
            sb.append(vector[i]);
            if (i < vector.length - 1) sb.append(",");
        }
        sb.append("]");
        return sb.toString();
    }

    // =========================
    // HELPER: ESCAPE JSON TEXT
    // =========================
    private String escapeJson(String text) {
        return text.replace("\"", "\\\"");
    }
}