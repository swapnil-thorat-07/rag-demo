package com.example.ingestion;

import java.util.List;
import java.util.Map;

public class IngestionPipeline {

    private final DocumentChunker chunker;
    private final MetadataExtractor metadataExtractor;

    public IngestionPipeline() {
        this.chunker = new DocumentChunker();
        this.metadataExtractor = new MetadataExtractor();
    }

    public void ingest(String url, String title, String text) {

        List<String> chunks = chunker.chunk(text, 500);

        Map<String, Object> metadata = metadataExtractor.extract(url, title);

        for (String chunk : chunks) {

            System.out.println("Chunk:");
            System.out.println(chunk.substring(0, Math.min(chunk.length(), 100)));

            System.out.println("Metadata: " + metadata);
        }
    }
}