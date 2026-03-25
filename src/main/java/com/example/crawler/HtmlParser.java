package com.example.crawler;

import com.example.ingestion.ChewyEducationChunker.CategoryBlock;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HtmlParser {

    public List<CategoryBlock> parseChewyEducation(String url) throws IOException {

        Document doc = Jsoup.connect(url).get();

        List<CategoryBlock> blocks = new ArrayList<>();

        // Only target real category headers
        for (Element header : doc.select("h2")) {

            String category = header.text().trim();

            // Skip non-category headers
            if (category.equalsIgnoreCase("All Creatures Covered")
                    || category.equalsIgnoreCase("About Chewy Education")
                    || category.length() < 3) {
                continue;
            }

            List<String> articles = new ArrayList<>();

            // 🔥 KEY FIX: Traverse siblings
            Node current = header.nextSibling();

            while (current != null) {

                if (current instanceof Element element) {

                    // Stop when next category starts
                    if (element.tagName().equals("h2")) {
                        break;
                    }

                    // Collect links
                    for (Element link : element.select("a")) {
                        String text = link.text().trim();

                        if (!text.isEmpty() && text.length() > 5) {
                            articles.add(text);
                        }
                    }
                }

                current = current.nextSibling();
            }

            if (!articles.isEmpty()) {
                blocks.add(new CategoryBlock(category, articles));
            }
        }

        return blocks;
    }
}