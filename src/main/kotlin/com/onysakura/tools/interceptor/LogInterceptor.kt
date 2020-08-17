package com.onysakura.tools.interceptor

import com.onysakura.tools.filter.BodyReaderHttpServletRequestWrapper
import com.onysakura.tools.filter.BodyReaderHttpServletResponseWrapper
import com.onysakura.tools.wechat.WechatUtils
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class LogInterceptor : HandlerInterceptor {

    val log: Logger = LoggerFactory.getLogger(LogInterceptor::class.java)

    val mapAdapter: JsonAdapter<Map<*, *>> = Moshi.Builder().add(KotlinJsonAdapterFactory()).build().adapter(Map::class.java)

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        var str = "[${request.session.id}]：${request.requestURI} request"
        if (request.parameterMap.isNotEmpty()) {
            str += ", params: ${mapAdapter.toJson(request.parameterMap)}"
        }
        try {
            val ip: String = WechatUtils.getIp(request)
            str += ", ip: $ip"
        } catch (e: Exception) {

        }
        if (request is BodyReaderHttpServletRequestWrapper) {
            val body: String? = request.getBody()
            if (!body.isNullOrBlank()) {
                str += ", body:\n$body"
            }
        }
        log.info("\u001B[95m$str\u001B[0m")
        return super.preHandle(request, response, handler)
    }

    override fun afterCompletion(request: HttpServletRequest, response: HttpServletResponse, handler: Any, ex: java.lang.Exception?) {
        var str = "[${request.session.id}]：${request.requestURI} response, status: ${response.status}"
        if (response is BodyReaderHttpServletResponseWrapper) {
            val responseData = String(response.getResponseData())
            if (!responseData.isBlank()) {
                str += ", body:\n$responseData"
            }
        }
        log.info("\u001B[35m$str\u001B[0m")
        super.afterCompletion(request, response, handler, ex)
    }
}