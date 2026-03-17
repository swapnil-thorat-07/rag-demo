package com.example.rag;
import com.example.crawler.ChewyScraper;
import com.example.crawler.HtmlParser;
import com.example.embeddings.EmbeddingGenerator;
import com.example.search.VectorSearchSingleton;
import dev.langchain4j.data.embedding.Embedding;
import java.nio.file.Path;
import java.util.List;
public class RAGPipeline {

    private static String apiKeyOpenAI = "";
    public String chat(String question) {
        ChewyScraper scrapper = new ChewyScraper();
        String answerContent = "";
        try {
            String pageText = "";
          /*  String pageContent = scrapper.fetchPage("https://www.chewy.com/education");
            System.out.println("HTML Page Content:\n"+pageContent);

            HtmlParser parser = new HtmlParser();
            pageText = parser.extractText(pageContent);
            System.out.println("pageText:\n"+pageText);
             answerContent = pageText;
            */
            EmbeddingGenerator embeddingGenerator = new EmbeddingGenerator(apiKeyOpenAI);
            Embedding embedding = embeddingGenerator.generate(pageText, true);

            float[] vector = embedding.vector();
            System.out.println("Embedding Vector:");
            for (float v : vector) {
                System.out.printf("%.6f%n", v);
            }


            VectorSearchSingleton vectorSearch = VectorSearchSingleton.getInstance();

            // Load from file (embedding.txt)
            //vectorSearch.loadFromFile(Path.of("embedding.txt"));
            vectorSearch.addEmbedding("doc1", embedding);

            // Suppose this is your cached 1536-dim vector for "Pets"
            float[] petsVector = new float[] {
                    0.009923f, 0.002446f, -0.002994f, -0.025563f, -0.019304f, 0.001069f, /* ... continue all 1536 floats ... */
                    // fill in the rest of the 1536 values
            };

            // Create a LangChain4j Embedding object
            Embedding queryEmbedding = new Embedding(petsVector);

            // Now you can use it in your vector search singleton
            System.out.println("Vector length: " + queryEmbedding.vector().length);

            // Search top 5
            List<VectorSearchSingleton.SearchResult> results = vectorSearch.search(queryEmbedding.vector(), 5);

            results.forEach(System.out::println);
        }catch(Exception e){
            answerContent = e.getMessage();
        }

        return "Question: " + question+" RAG Response:"+answerContent;
    }

}