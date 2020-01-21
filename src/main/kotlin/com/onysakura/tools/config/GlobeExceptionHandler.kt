package com.onysakura.tools.config

import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport

@ControllerAdvice
open class GlobeExceptionHandler : WebMvcConfigurationSupport() {

    @ExceptionHandler(value = [Exception::class])
    fun configureContentNegotiation(e: Exception): String {
        return "Error: " + e.message
    }
}