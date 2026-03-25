package com.example.ingestion;

import java.util.ArrayList;
import java.util.List;

public class ChewyEducationChunker {

    public static class Chunk {
        private String category;
        private String content;

        public Chunk(String category, String content) {
            this.category = category;
            this.content = content;
        }

        public String getCategory() {
            return category;
        }

        public String getContent() {
            return content;
        }

        @Override
        public String toString() {
            return "Category: " + category + "\n" + content;
        }
    }

    /**
     * Build chunks from category -> article titles
     */
    public List<Chunk> chunkByCategory(List<CategoryBlock> blocks) {

        List<Chunk> chunks = new ArrayList<>();

        for (CategoryBlock block : blocks) {

            StringBuilder sb = new StringBuilder();

            sb.append("Category: ").append(block.getCategory()).append("\n\n");
            sb.append("Articles:\n");

            for (String article : block.getArticles()) {
                sb.append("- ").append(article).append("\n");
            }

            chunks.add(new Chunk(block.getCategory(), sb.toString()));
        }

        return chunks;
    }

    /**
     * Helper DTO (you map this from your HtmlParser)
     */
    public static class CategoryBlock {
        private String category;
        private List<String> articles;

        public CategoryBlock(String category, List<String> articles) {
            this.category = category;
            this.articles = articles;
        }

        public String getCategory() {
            return category;
        }

        public List<String> getArticles() {
            return articles;
        }
    }
}