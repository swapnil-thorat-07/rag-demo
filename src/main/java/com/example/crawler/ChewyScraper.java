package com.example.crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import java.io.IOException;

public class ChewyScraper {

    public String fetchPage(String url) throws IOException {
        String html = "";
        try {
         /* Document doc = Jsoup.connect(url)
                .userAgent("Mozilla/5.0")
                .timeout(10000)
                .get();*/

         WebDriver driver = new ChromeDriver();
         driver.get("https://www.chewy.com/education");

         // wait for JS to load
         Thread.sleep(5000);

         html = driver.getPageSource();
     }
     catch(Exception e){
         System.out.println("exception:"+e.getMessage());
     }
        Document doc = Jsoup.parse(html);
        return doc.html();
    }
}