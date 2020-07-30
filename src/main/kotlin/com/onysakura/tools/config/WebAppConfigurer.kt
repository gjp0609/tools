package com.onysakura.tools.config

import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Configuration
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
@ControllerAdvice
open class WebAppConfigurer : WebMvcConfigurer {
    val log = LoggerFactory.getLogger(WebAppConfigurer::class.java)

    @ExceptionHandler(value = [Exception::class])
    @ResponseBody
    fun configureContentNegotiation(e: Exception): String {
        log.warn("error", e)
        return "Error: " + e.message
    }
}