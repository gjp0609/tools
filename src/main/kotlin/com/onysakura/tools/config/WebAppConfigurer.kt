package com.onysakura.tools.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
@ControllerAdvice
open class WebAppConfigurer : WebMvcConfigurer {

    @ExceptionHandler(value = [Exception::class])
    @ResponseBody
    fun configureContentNegotiation(e: Exception): String {
        e.printStackTrace()
        return "Error: " + e.message
    }
}