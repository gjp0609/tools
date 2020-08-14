package com.onysakura.tools.config

import com.onysakura.tools.interceptor.LogInterceptor
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Configuration
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
@ControllerAdvice
open class WebAppConfigurer : WebMvcConfigurer {
    val log = LoggerFactory.getLogger(WebAppConfigurer::class.java)
    val mapAdapter: JsonAdapter<Map<*, *>> = Moshi.Builder().add(KotlinJsonAdapterFactory()).build().adapter(Map::class.java)


    @ExceptionHandler(value = [Exception::class])
    @ResponseBody
    fun configureContentNegotiation(e: Exception): String {
        log.warn("error", e)
        return "Error: " + e.message
    }

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(LogInterceptor())
        super.addInterceptors(registry)
    }
}