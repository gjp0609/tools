package com.onysakura.tools.kindle

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.format.datetime.DateFormatter
import org.springframework.stereotype.Component
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Duration
import java.util.*

@Component
class Job(private val articleRepository: ArticleRepository) {
    val logger: Logger = LoggerFactory.getLogger(Job::class.java)

    //    @Scheduled(cron = "23 52 0/6 * * ?")
    fun job() {
        logger.info("job start")
        mit(System.currentTimeMillis())
    }

    fun mit(startTime: Long) {
        val url = "https://m.ithome.com/api/news/newslistpageget?Tag=&ot=$startTime&page=0"
        val formatter = DateFormatter()
        formatter.setPattern("yyyy-MM-dd HH:mm:ss")
        val datePrint = formatter.print(Date(startTime), Locale.CHINA)
        logger.info("mit: $datePrint")
        val client = HttpClient
                .newBuilder()
                .connectTimeout(Duration.ofMillis(5000))
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build()
        val uri = URI.create(url)
        val request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .header("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/11.0 Mobile/15A372 Safari/604.1")
                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3")
                .header("Upgrade-Insecure-Requests", "1")
                .timeout(Duration.ofMillis(50000))
                .build()
        val send = client.send(request, HttpResponse.BodyHandlers.ofString())
        val mapAdapter = Moshi.Builder().add(KotlinJsonAdapterFactory()).build().adapter(Map::class.java)
        val map = mapAdapter.fromJson(send.body())
        if (map?.get("Success") as Double == 1.0) {
            val list = map["Result"] as Collection<*>
            val articleList = mutableListOf<Article>()
            var minDate = Date()
            var repeatCount = 0
            list.forEach {
                it as Map<*, *>
                if (!it.toString().contains("TipName=广告")) {
                    val id = (it["newsid"] as Double).toInt().toString()
                    if (articleRepository.existsById(id)) {
                        repeatCount++
                    } else {
                        val article = Article()
                        article.id = id
                        article.title = it["title"] as String
                        article.url = it["WapNewsUrl"] as String
                        val orderDate = it["orderdate"] as String
                        formatter.setPattern("yyyy-MM-dd'T'HH:mm:ss")
                        val parse = formatter.parse(orderDate.substring(0, 19), Locale.CHINA)
                        if (minDate.after(parse)) {
                            minDate = parse
                        }
                        articleList.add(article)
                    }
                }
            }
            articleRepository.saveAll(articleList)
            if (repeatCount < 5 && System.currentTimeMillis() - startTime < 1000 * 60 * 60 * 24) {
                Thread.sleep(10000)
                mit(minDate.time)
            } else {
                logger.info("mit: Done!")
            }
        }
    }
}