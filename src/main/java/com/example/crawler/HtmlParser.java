package com.example.crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class HtmlParser {

    public String extractText(String html) {

        Document doc = Jsoup.parse(html);

        doc.select("script,style,nav,footer,header").remove();

        return doc.body().text();
    }
}