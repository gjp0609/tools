package com.onysakura.tools.kindle

import org.jsoup.Jsoup
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.RequestMapping
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Component
@RequestMapping
class Job(private val articleRepository: ArticleRepository) {

    @Scheduled(cron = "0 * * * * ?")
    fun job123() {
        it(1)
    }

    fun it(startIndex: Int) {
        val url = "https://www.ithome.com"
        var path = "/list/"
        if (startIndex != 1) {
            path += "list_$startIndex.html"
        }
        println(url + path)
        val articleList = mutableListOf<Article>()
        val document = Jsoup.connect(url + path)
                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3")
                .header("Accept-Encoding", "ggzip, deflate, br")
                .header(URLEncoder.encode(":path", StandardCharsets.UTF_8), path)
                .header(URLEncoder.encode(":authority", StandardCharsets.UTF_8), "www.ithome.com")
                .header(URLEncoder.encode(":method", StandardCharsets.UTF_8), "GET")
                .header(URLEncoder.encode(":scheme", StandardCharsets.UTF_8), "https")
                .header("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8,ja;q=0.7,und;q=0.6")
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36")
                .timeout(5000)
                .get()
        val aList = document.select(".post_list").select("ul").select("li").select(">a")
        var repeatCount = 0
        for (a in aList) {
            val href = a.attr("href")
            if (href != null && !href.contains("lapin")) {
                val start = "0/"
                var urlTemp = href.substring(href.lastIndexOf(start))
                urlTemp = urlTemp.substring(start.length, urlTemp.lastIndexOf("."))
                val id = "ithome" + urlTemp.replace("/", "")
                if (articleRepository.existsById(id)) {
                    repeatCount++
                    continue
                }
                val article = Article()
                article.id = id
                article.title = a.text()
                article.url = href
                articleList.add(article)
            }
        }
        println(articleList.size)
        articleRepository.saveAll(articleList)
        if (repeatCount < 5 && startIndex < 10) {
            Thread.sleep(10000)
            it(startIndex + 1)
        } else {
            println("Done!")
        }
    }
}

fun main() {
    val url = "https://www.ithome.com/0/459/505.htm"
    val document = Jsoup.connect(url )
            .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3")
            .header("Accept-Encoding", "ggzip, deflate, br")
//            .header(URLEncoder.encode(":path", StandardCharsets.UTF_8), "")
            .header(URLEncoder.encode(":authority", StandardCharsets.UTF_8), "www.ithome.com")
            .header(URLEncoder.encode(":method", StandardCharsets.UTF_8), "GET")
            .header(URLEncoder.encode(":scheme", StandardCharsets.UTF_8), "https")
            .header("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8,ja;q=0.7,und;q=0.6")
            .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36")
            .timeout(5000)
            .get()
    println(document.select(".content").select(">.post_content"))
}