package com.example.api;

import com.example.embeddings.EmbeddingGenerator;
import com.example.embeddings.PineconeStore;
import dev.langchain4j.data.embedding.Embedding;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ContentIngestionService {

    private final EmbeddingGenerator embeddingGenerator;
    private final PineconeStore pineconeStore;

    public ContentIngestionService(EmbeddingGenerator embeddingGenerator,
                                   PineconeStore pineconeStore) {
        this.embeddingGenerator = embeddingGenerator;
        this.pineconeStore = pineconeStore;
    }

    public ContentIngestionResponse ingest(ContentIngestionRequest request) throws Exception {

        String text = request.getTextContent();

        List<String> chunks = chunkText(text, 500); // simple chunking

        int storedCount = 0;

        for (String chunk : chunks) {

            Embedding embedding = embeddingGenerator.generate(chunk, false);

            float[] vector = embedding.vector();

            pineconeStore.store(chunk, vector);

            storedCount++;
        }

        return new ContentIngestionResponse("SUCCESS", storedCount);
    }

    /**
     * Simple chunking logic (can be replaced with smarter one)
     */
    private List<String> chunkText(String text, int maxChunkSize) {

        List<String> chunks = new ArrayList<>();

        int start = 0;

        while (start < text.length()) {
            int end = Math.min(start + maxChunkSize, text.length());
            chunks.add(text.substring(start, end));
            start = end;
        }

        return chunks;
    }
}