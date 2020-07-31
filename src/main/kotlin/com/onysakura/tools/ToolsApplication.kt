package com.onysakura.tools;

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

@SpringBootApplication
@EnableScheduling
open class ToolsApplication

fun main(args: Array<String>) {
    runApplication<ToolsApplication>(*args)

    val uri: URI = URI.create("https://www.baidu.com")
    val response: HttpResponse<String> = HttpClient.newHttpClient()
            .send(HttpRequest.newBuilder().uri(uri).build(), HttpResponse.BodyHandlers.ofString())
    println(response)
    println(response.body())
}
