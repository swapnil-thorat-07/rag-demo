package com.example.crawler;

import java.util.List;

public class CrawlScheduler {

    private final ChewyScraper scraper;
    private final HtmlParser parser;

    public CrawlScheduler() {
        this.scraper = new ChewyScraper();
        this.parser = new HtmlParser();
    }

    public void crawl(List<String> urls) {

        for (String url : urls) {
            try {

                String html = scraper.fetchPage(url);

                String text = parser.extractText(html);

                System.out.println("Crawled: " + url);
                System.out.println(text.substring(0, Math.min(text.length(), 200)));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}