package com.onysakura.tools.wechat

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.net.URI

@ConditionalOnProperty("custom.features-enable.wechat")
@Component
class WechatJob {

    val log = LoggerFactory.getLogger(WechatJob::class.java)

    @Scheduled(cron = "0 0 0/2 * * ?") // 2 hours
    fun getTokenAndTicketJob() {
        val mutableMapAdapter = Moshi.Builder().add(KotlinJsonAdapterFactory()).build().adapter(MutableMap::class.java)
        try {
            val uri = URI.create(WechatUtils.ACCESS_TOKEN_URL.replace("APP_ID", WechatUtils.APP_ID).replace("APP_SECRET", WechatUtils.APP_SECRET))
            log.debug("get access_token uri: $uri")
            val response = uri.toURL().readText()
            log.info("response: $response")
            WechatUtils.ACCESS_TOKEN_AND_TICKET["access_token"] = mutableMapAdapter.fromJson(response)?.get("access_token") as String
        } catch (e: Exception) {
            log.warn("get access_token error", e)
        }
        try {
            val accessToken = WechatUtils.ACCESS_TOKEN_AND_TICKET["access_token"]
            if (WechatUtils.ACCESS_TOKEN_AND_TICKET["access_token"] == null) {
                throw RuntimeException("no access_token")
            }
            val uri = URI.create(WechatUtils.JS_API_TICKET_URL.replace("ACCESS_TOKEN", accessToken as String))
            log.debug("get jsapi_ticket uri: $uri")
            val response = uri.toURL().readText()
            log.info("response: $response")
            WechatUtils.ACCESS_TOKEN_AND_TICKET["jsapi_ticket"] = mutableMapAdapter.fromJson(response)?.get("ticket") as String
        } catch (e: Exception) {
            log.warn("get jsapi_ticket error", e)
        }
    }
}