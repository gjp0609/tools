package com.onysakura.tools.kindle

import org.jsoup.Jsoup
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import org.springframework.web.bind.annotation.RequestMapping
import java.net.URI
import java.net.URLEncoder
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.nio.charset.StandardCharsets
import java.time.Duration
import java.util.*

@Component
@RequestMapping
class Job {

    fun searchApi(q: String, start: Int): HashMap<String, Any> {
        val timeMillis = System.currentTimeMillis()
        val map = HashMap<String, Any>()
        val arrayList = ArrayList<HashMap<String, String?>>()
        if (!StringUtils.isEmpty(q)) {
            val googleUrl = "https://www.google.com/search?q="
            val client = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofMillis(5000))
                    .followRedirects(HttpClient.Redirect.NORMAL)
                    .build()
            val uri = URI.create(googleUrl + URLEncoder.encode(q, StandardCharsets.UTF_8) + "&start=" + start)
            val request = HttpRequest.newBuilder()
                    .uri(uri)
                    .GET()
                    .header("content-type", "application/json; charset=UTF-8")
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36")
                    .header("accept-language", "zh-CN,zh;q=0.9,en;q=0.8,ja;q=0.7,und;q=0.6")
                    .timeout(Duration.ofMillis(50000)).build()
            val send = client.send(request, HttpResponse.BodyHandlers.ofInputStream())
            val document = Jsoup.parse(send.body(), "UTF-8", "https://www.google.com")
            val body = document.body()
            val rcList = body.select(".rc")
            for (rc in rcList) {
                val r = rc.select(".r")
                val a = r.select("a")
                var title: String? = null
                var url: String? = null
                var link: String? = null
                if (a.size > 0) {
                    val element = a[0]
                    link = element.attr("href")
                    val h3 = element.select("h3")
                    title = h3.text()
                    val cite = element.select("cite")
                    if (cite.size > 0) {
                        url = cite[0].text()
                    }
                }
                val s = rc.select(".s")
                val hashMap = HashMap<String, String?>()
                hashMap["title"] = title
                hashMap["url"] = url
                hashMap["link"] = link
                hashMap["text"] = s.text()
                arrayList.add(hashMap)
            }
        }
        val timeUsage = System.currentTimeMillis() - timeMillis
        map["timeUsage"] = timeUsage
        map["resultList"] = arrayList
        return map
    }
}