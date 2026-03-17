package com.example.rag;
import com.example.crawler.ChewyScraper;
import com.example.crawler.HtmlParser;
import com.example.embeddings.EmbeddingGenerator;
import dev.langchain4j.data.embedding.Embedding;

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
        }catch(Exception e){
            answerContent = e.getMessage();
        }

        return "Question: " + question+" RAG Response:"+answerContent;
    }

}