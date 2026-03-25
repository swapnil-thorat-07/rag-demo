package com.example.crawler;

import com.example.ingestion.ChewyEducationChunker.CategoryBlock;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HtmlParser {

    /**
     * Main method to parse Chewy Education page
     */
    public List<CategoryBlock> parseChewyEducation(String url) throws IOException {

        Document doc = Jsoup.connect(url).get();

        List<CategoryBlock> blocks = new ArrayList<>();

        // 🔍 Inspecting Chewy page:
        // Categories are usually in sections with headings (h2/h3)
        Elements categoryHeaders = doc.select("h2, h3");

        for (Element header : categoryHeaders) {

            String category = header.text().trim();

            // Skip useless headers
            if (category.isEmpty() || category.length() < 3) continue;

            // Get the container after header
            Element parentSection = header.parent();

            if (parentSection == null) continue;

            // Extract links (articles)
            Elements links = parentSection.select("a");

            List<String> articles = new ArrayList<>();

            for (Element link : links) {
                String text = link.text().trim();

                // Filter noise
                if (!text.isEmpty() && text.length() > 5) {
                    articles.add(text);
                }
            }

            // Only keep meaningful categories
            if (!articles.isEmpty() && articles.size() > 2) {
                blocks.add(new CategoryBlock(category, articles));
            }
        }

        return blocks;
    }
}