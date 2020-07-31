package com.onysakura.tools;

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
open class ToolsApplication

fun main(args: Array<String>) {
    runApplication<ToolsApplication>(*args)

    GetTest.get("http://httpbin.org/get?param=test")
}
