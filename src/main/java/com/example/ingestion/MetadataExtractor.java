package com.example.ingestion;

import java.util.HashMap;
import java.util.Map;

public class MetadataExtractor {

    public Map<String, Object> extract(String url, String title) {

        Map<String, Object> metadata = new HashMap<>();

        metadata.put("url", url);
        metadata.put("title", title);
        metadata.put("source", "chewy");

        return metadata;
    }
}