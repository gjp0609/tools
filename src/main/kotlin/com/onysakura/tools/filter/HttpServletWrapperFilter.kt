package com.onysakura.tools.filter

import org.springframework.web.multipart.MultipartHttpServletRequest
import org.springframework.web.multipart.commons.CommonsMultipartResolver
import javax.servlet.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class HttpServletWrapperFilter : Filter {

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        var requestWrapper: ServletRequest? = null
        var responseWrapper: BodyReaderHttpServletResponseWrapper? = null
        if (request is HttpServletRequest) {
            if (arrayOf("POST", "PUT").contains(request.method)) {
                if (request.contentType.contains("multipart/form-data")) {
                    val multipartResolver = CommonsMultipartResolver()
                    val multipartRequest: MultipartHttpServletRequest = multipartResolver.resolveMultipart(request)
                    requestWrapper = multipartRequest
                } else {
                    requestWrapper = BodyReaderHttpServletRequestWrapper(request)
                }
            }
        }
        if (response is HttpServletResponse) {
            responseWrapper = BodyReaderHttpServletResponseWrapper(response)
        }
        chain.doFilter(requestWrapper ?: request, responseWrapper ?: response)
        val resp: ByteArray? = responseWrapper?.getResponseData()
        val output: ServletOutputStream = response.outputStream
        if (resp != null) {
            output.write(resp)
            output.flush()
        }
    }

}