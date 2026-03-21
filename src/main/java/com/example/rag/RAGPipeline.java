package com.example.rag;
import com.example.crawler.ChewyScraper;
import com.example.crawler.HtmlParser;
import com.example.embeddings.EmbeddingGenerator;
import com.example.embeddings.PineconeStore;
import com.example.search.VectorSearchSingleton;
import dev.langchain4j.data.embedding.Embedding;
import java.nio.file.Path;
import java.util.List;
import java.util.Arrays;
public class RAGPipeline {

    private static String OPEN_AI_API_KEY = "<Redacted>";
    private static String PINECONE_API_KEY = "<Redacted>";
    private static String PINECONE_HOST = "https://default-fxdi3dn.svc.aped-4627-b74a.pinecone.io";

    public String chat(String question) {
        ChewyScraper scrapper = new ChewyScraper();
        StringBuilder answer = new StringBuilder();
        try {
            String pageText = "";
          /*  String pageContent = scrapper.fetchPage("https://www.chewy.com/education");
            System.out.println("HTML Page Content:\n"+pageContent);

            HtmlParser parser = new HtmlParser();
            pageText = parser.extractText(pageContent);
            System.out.println("pageText:\n"+pageText);
             answerContent = pageText;
            */
            EmbeddingGenerator embeddingGenerator = new EmbeddingGenerator(OPEN_AI_API_KEY);
            Embedding pageEmbedding = embeddingGenerator.generate(pageText, true);

            PineconeStore pineconeStore = new PineconeStore(PINECONE_API_KEY, PINECONE_HOST);
            String text = "";
            text = question;
            Embedding embedding = embeddingGenerator.generate(text, false);
            float[] vector = embedding.vector();
            pineconeStore.store(text, vector);

            Embedding queryEmbedding = embeddingGenerator.generate(question, false);
            // Now you can use it in your vector search singleton
            System.out.println("question: "+question);
            System.out.println("Vector length: " + queryEmbedding.vector().length);
//            System.out.println("Vector Array: " + Arrays.toString(queryEmbedding.vector()));
            System.out.println();

            // Query raw JSON
            String json = pineconeStore.query(queryEmbedding.vector(), 5);
            System.out.println("Query Json:"+ json);
            System.out.println("Query pineconeStore.queryAndExtractTexts:");

            //Query clean texts
            List<String> texts = pineconeStore.queryAndExtractTexts(queryEmbedding.vector(), 5);
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

}