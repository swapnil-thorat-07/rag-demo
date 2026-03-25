package com.example.rag;
import com.example.crawler.ChewyScraper;
import com.example.crawler.HtmlParser;
import com.example.embeddings.EmbeddingGenerator;
import com.example.embeddings.PineconeStore;
import com.example.ingestion.ChewyEducationChunker;
import com.example.ingestion.ChewyEducationChunker.CategoryBlock;
import com.example.search.VectorSearchSingleton;
import com.example.utils.Constants;
import dev.langchain4j.data.embedding.Embedding;
import java.nio.file.Path;
import java.util.List;
import java.util.Arrays;
public class RAGPipeline {

    public String chat(String question) {
        ChewyScraper scrapper = new ChewyScraper();
        StringBuilder answer = new StringBuilder();
        try {
            String pageText = "";
            String pageContent = scrapper.fetchPage("https://www.chewy.com/education");
            System.out.println("HTML Page Content:\n"+pageContent);

            HtmlParser parser = new HtmlParser();

            List<CategoryBlock> blocks =
                    parser.parseChewyEducation("https://www.chewy.com/education");
            System.out.println("blocks:"+blocks);
            ChewyEducationChunker chunker = new ChewyEducationChunker();
            var chunks = chunker.chunkByCategory(blocks);
      //      System.out.println("pageText:\n"+pageText);
            System.out.println("chunks:\n"+chunks);

            EmbeddingGenerator embeddingGenerator = new EmbeddingGenerator(Constants.OPEN_AI_API_KEY);
            Embedding pageEmbedding = embeddingGenerator.generate(pageText, true);

            PineconeStore pineconeStore = new PineconeStore(Constants.PINECONE_API_KEY, Constants.PINECONE_HOST);
            String text = "";
            text = question;
            Embedding embedding = embeddingGenerator.generate(text, false);
            float[] vector = embedding.vector();
            vector = normalize(vector);
            pineconeStore.store(text, vector);

            Embedding queryEmbedding = embeddingGenerator.generate(question, false);
            // Now you can use it in your vector search singleton
            System.out.println("question: "+question);
            System.out.println("Vector length: " + queryEmbedding.vector().length);
//            System.out.println("Vector Array: " + Arrays.toString(queryEmbedding.vector()));
            System.out.println();

            float queryVector[] = queryEmbedding.vector();
            queryVector = normalize(queryVector);
            // Query raw JSON
            String json = pineconeStore.query(queryVector, 5);
            System.out.println("Query Json:"+ json);
            System.out.println("Query pineconeStore.queryAndExtractTexts:");

            //Query clean texts
            List<String> texts = pineconeStore.queryAndExtractTexts(queryVector, 5);
            for (String t : texts) {
                System.out.println(t);
            }
            /*
            System.out.println("Embedding Vector:");
            for (float v : vector) {
                System.out.printf("%.6f%n", v);
            }


            VectorSearchSingleton vectorSearch = VectorSearchSingleton.getInstance();

            // Load from file (embedding.txt)
            //vectorSearch.loadFromFile(Path.of("embedding.txt"));
            vectorSearch.addEmbedding("doc1", embedding);

            // Search top 5
            List<VectorSearchSingleton.SearchResult> results = vectorSearch.search(queryEmbedding.vector(), 5);
            // Iterate and append
            for (VectorSearchSingleton.SearchResult result : results) {
                answer.append("ID: ").append(result.id)
                        .append(", Score: ").append(result.score)
                        .append("\n");
            }*/
        }catch(Exception e){
            answer.append(e.getMessage());
        }

        return "Question: " + question+" RAG Response:"+answer.toString();
    }

    public float[] normalize(float[] vector) {
        double sum = 0.0;
        for (float v : vector) {
            sum += v * v;
        }
        double norm = Math.sqrt(sum);

        float[] result = new float[vector.length];
        for (int i = 0; i < vector.length; i++) {
            result[i] = (float) (vector[i] / norm);
        }
        return result;
    }
}