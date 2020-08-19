package com.onysakura.tools.config

import com.onysakura.tools.common.Result
import com.onysakura.tools.common.ServiceException
import com.onysakura.tools.interceptor.LogInterceptor
import org.slf4j.Logger
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
    val log: Logger = LoggerFactory.getLogger(WebAppConfigurer::class.java)

    @ExceptionHandler(value = [Exception::class])
    @ResponseBody
    fun configureContentNegotiation(e: Exception): Result<*> {
        if (e !is ServiceException) {
            log.warn("error", e)
        }
        return Result.error("Error: " + e.message)
    }

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(LogInterceptor())
        super.addInterceptors(registry)
    }
}