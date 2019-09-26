package com.onysakura.tools.search;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;

@RestController
@RequestMapping("/search")
public class SearchController {

    @GetMapping
    public Object main(String q) throws Exception {
        long timeMillis = System.currentTimeMillis();
        HashMap<Object, Object> map = new HashMap<>();
        String googleUrl = "https://www.google.com/search?q=";
        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofMillis(5000))
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();
        URI uri = URI.create(googleUrl + q);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .header("content-type", "application/json; charset=UTF-8")
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36")
                .timeout(Duration.ofMillis(50000)).build();
        HttpResponse<InputStream> send = client.send(request, HttpResponse.BodyHandlers.ofInputStream());
        Document document = Jsoup.parse(send.body(), "UTF-8", "https://www.google.com");
        Element body = document.body();
        Elements rcList = body.select(".rc");
        ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
        for (Element rc : rcList) {
            Elements r = rc.select(".r");
            Elements a = r.select("a");
            String title = null;
            String url = null;
            String link = null;
            if (a.size() > 0) {
                Element element = a.get(0);
                link = element.attr("href");
                Elements h3 = element.select("h3");
                title = h3.text();
                Elements cite = element.select("cite");
                if (cite.size() > 0) {
                    url = cite.get(0).text();
                }
            }
            Elements s = rc.select(".s");
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("title", title);
            hashMap.put("url", url);
            hashMap.put("link", link);
            hashMap.put("text", s.text());
            arrayList.add(hashMap);
        }
        long timeUsage = System.currentTimeMillis() - timeMillis;
        map.put("timeUsage", timeUsage);
        map.put("resultList", arrayList);
        return map;
    }
}
