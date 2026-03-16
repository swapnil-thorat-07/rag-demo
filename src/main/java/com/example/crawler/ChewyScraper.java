package com.example.crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class ChewyScraper {

    public String fetchPage(String url) throws IOException {
        Document doc = Jsoup.connect(url)
                .userAgent("Mozilla/5.0")
                .timeout(10000)
                .get();

        return doc.html();
    }
}