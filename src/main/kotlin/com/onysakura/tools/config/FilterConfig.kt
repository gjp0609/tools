package com.onysakura.tools.config

import com.onysakura.tools.filter.HttpServletWrapperFilter
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.servlet.Filter

@Configuration
open class FilterConfig {

    @Bean
    open fun httpServletRequestWrapperFilter(): FilterRegistrationBean<Filter> {
        val registration = FilterRegistrationBean<Filter>()
        registration.filter = HttpServletWrapperFilter()
        registration.addUrlPatterns("/*")
        registration.setName("LogCostFilter")
        registration.order = 1
        return registration
    }

}