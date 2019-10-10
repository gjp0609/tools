package com.onysakura.tools.search;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.io.InputStream;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;

@Controller
@RequestMapping("/search")
public class SearchController {

    @GetMapping
    public ModelAndView search(ModelAndView mv, String q, @RequestParam(required = false, defaultValue = "0") int start) throws Exception {
        HashMap<String, Object> map = searchApi(q, start);
        mv.addObject("ad", "asd");
        mv.addObject("timeUsage", map.get("timeUsage"));
        mv.addObject("resultList", map.get("resultList"));
        mv.addObject("searchText", q);
        mv.setViewName("search");
        return mv;
    }

    @GetMapping("/api")
    public HashMap<String, Object> searchApi(String q, @RequestParam(required = false, defaultValue = "0") int start) throws Exception {
        long timeMillis = System.currentTimeMillis();
        HashMap<String, Object> map = new HashMap<>();
        ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
        if (!StringUtils.isEmpty(q)) {
            String googleUrl = "https://www.google.com/search?q=";
            HttpClient client = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofMillis(5000))
                    .followRedirects(HttpClient.Redirect.NORMAL)
                    .build();
            URI uri = URI.create(googleUrl + URLEncoder.encode(q, StandardCharsets.UTF_8) + "&start=" + start);
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
        }
        long timeUsage = System.currentTimeMillis() - timeMillis;
        map.put("timeUsage", timeUsage);
        map.put("resultList", arrayList);
        return map;
    }
}
