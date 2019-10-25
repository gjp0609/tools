package com.onysakura.tools;

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class ToolsApplication

fun main(args: Array<String>) {
    runApplication<ToolsApplication>(*args)
}
