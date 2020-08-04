package com.onysakura.tools.config

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Configuration
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

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
        registry.addInterceptor(object : HandlerInterceptor {
            override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
                log.info("\u001B[95m[${request.session.id}]：${request.requestURI} request, params: ${mapAdapter.toJson(request.parameterMap)}\u001B[0m")
                return super.preHandle(request, response, handler)
            }

            override fun afterCompletion(request: HttpServletRequest, response: HttpServletResponse, handler: Any, ex: java.lang.Exception?) {
                log.info("\u001B[35m[${request.session.id}]：${request.requestURI} response, status: ${response.status}\u001B[0m")
                super.afterCompletion(request, response, handler, ex)
            }
        })
        super.addInterceptors(registry)
    }
}