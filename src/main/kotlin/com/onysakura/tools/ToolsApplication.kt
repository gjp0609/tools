package com.onysakura.tools;

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling
import java.net.HttpURLConnection
import java.net.URL

@SpringBootApplication
@EnableScheduling
open class ToolsApplication

fun main(args: Array<String>) {

    val url = URL("https://www.baidu.com/")

//    with(url.openConnection() as HttpURLConnection) {
//        requestMethod = "GET"
//
//        println("\nSent 'GET' request to URL : $url; Response Code : $responseCode")
//
//        inputStream.bufferedReader().use {
//            it.lines().forEach { line ->
//                println(line)
//            }
//        }
//    }

    runApplication<ToolsApplication>(*args)

    with(url.openConnection() as HttpURLConnection) {
        requestMethod = "GET"

        println("\nSent 'GET' request to URL : $url; Response Code : $responseCode")

        inputStream.bufferedReader().use {
            it.lines().forEach { line ->
                println(line)
            }
        }
    }
}
