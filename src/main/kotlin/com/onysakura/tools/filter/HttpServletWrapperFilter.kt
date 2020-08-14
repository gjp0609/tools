package com.onysakura.tools.filter

import javax.servlet.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


class HttpServletWrapperFilter : Filter {

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        var requestWrapper: ServletRequest? = null
        var responseWrapper: BodyReaderHttpServletResponseWrapper? = null
        if (request is HttpServletRequest) {
            //遇到post方法才对request进行包装
            val methodType: String = request.method
            if (arrayOf("POST", "PUT").contains(methodType)) {
                requestWrapper = BodyReaderHttpServletRequestWrapper(request)
            }
        }
        if (response is HttpServletResponse) {
            responseWrapper = BodyReaderHttpServletResponseWrapper(response)
        }
        chain.doFilter(requestWrapper ?: request, responseWrapper ?: response)
        val resp = responseWrapper?.getResponseData()
        val output: ServletOutputStream = response.outputStream
        if (resp != null) {
            output.write(resp)
            output.flush()
        }
    }

}