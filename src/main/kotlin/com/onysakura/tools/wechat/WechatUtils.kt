package com.onysakura.tools.wechat

import com.google.gson.Gson
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Duration
import javax.servlet.http.HttpServletRequest

@Component
open class WechatUtils {

    companion object {
        const val AUTH_URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECTURI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect"
        const val MESSAGE_SEND_URL = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=ACCESS_TOKEN"
        const val TEMPLATE_SEND_URL = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN"
        const val APP_ID = "wx6840b5bb18a51971"
        const val APP_SECRET = "79e3e8ee8432652c63d011b7b209c58d"
        const val ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET"
        const val JS_API_TICKET_URL = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi"
        const val TEXT_TEMPLATE = "6GPHNlSx8RV7oRDad5o7YxAR8eAOk0HBykn7NUO9ja8"
        const val OPEN_ID = "onaabxNXL5xgb5bEgtjOesv2f4wI"
        val ACCESS_TOKEN_AND_TICKET = mutableMapOf<String, String>()
        val client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofMillis(5000))
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build()!!

        fun sendMessage(text: String): String {
            val uri = URI.create(MESSAGE_SEND_URL.replace("ACCESS_TOKEN", ACCESS_TOKEN_AND_TICKET["access_token"] as String))
            val request = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString("""
                        {
                            "touser":"$OPEN_ID",
                            "msgtype":"text",
                            "text":
                            {
                                 "content":"$text"
                            }
                        }
                    """.trimIndent()))
                    .uri(uri)
                    .timeout(Duration.ofMillis(50000))
                    .build()
            return client.send(request, HttpResponse.BodyHandlers.ofString()).body()
        }

        fun sendTemplate(text: String): String {
            val uri = URI.create(TEMPLATE_SEND_URL.replace("ACCESS_TOKEN", ACCESS_TOKEN_AND_TICKET["access_token"] as String))
            val request = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString("""
                        {
                            "touser":"$OPEN_ID",
                            "template_id":"$TEXT_TEMPLATE",
                            "data":{
                                "text": {
                                    "value":"$text",
                                    "color":"#66ccff"
                                }
                            }
                        }
                    """.trimIndent()))
                    .uri(uri)
                    .timeout(Duration.ofMillis(50000))
                    .build()
            return client.send(request, HttpResponse.BodyHandlers.ofString()).body()
        }

        fun getIp(request: HttpServletRequest): String {
            var ip = request.getHeader("X-Forwarded-For")
            if (ip == null || ip.isEmpty() || ip.equals("unknown", ignoreCase = true)) {
                ip = request.getHeader("Proxy-Client-IP")
            }
            if (ip == null || ip.isEmpty() || ip.equals("unknown", ignoreCase = true)) {
                ip = request.getHeader("WL-Proxy-Client-IP")
            }
            if (ip == null || ip.isEmpty() || ip.equals("unknown", ignoreCase = true)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR")
            }
            if (ip == null || ip.isEmpty() || ip.equals("unknown", ignoreCase = true)) {
                ip = request.getHeader("HTTP_X_FORWARDED")
            }
            if (ip == null || ip.isEmpty() || ip.equals("unknown", ignoreCase = true)) {
                ip = request.getHeader("HTTP_X_CLUSTER_CLIENT_IP")
            }
            if (ip == null || ip.isEmpty() || ip.equals("unknown", ignoreCase = true)) {
                ip = request.getHeader("HTTP_CLIENT_IP")
            }
            if (ip == null || ip.isEmpty() || ip.equals("unknown", ignoreCase = true)) {
                ip = request.getHeader("HTTP_FORWARDED_FOR")
            }
            if (ip == null || ip.isEmpty() || ip.equals("unknown", ignoreCase = true)) {
                ip = request.getHeader("HTTP_FORWARDED")
            }
            if (ip == null || ip.isEmpty() || ip.equals("unknown", ignoreCase = true)) {
                ip = request.getHeader("HTTP_VIA")
            }
            if (ip == null || ip.isEmpty() || ip.equals("unknown", ignoreCase = true)) {
                ip = request.getHeader("REMOTE_ADDR")
            }
            if (ip == null || ip.isEmpty() || ip.equals("unknown", ignoreCase = true)) {
                ip = request.remoteAddr
            }
            return ip
        }
    }

    @Scheduled(fixedRate = 1000 * 60 * 60 * 2) // 2 hours
    fun getTokenAndTicketJob() {
        kotlin.run {
            val uri = URI.create(ACCESS_TOKEN_URL.replace("APPID", APP_ID).replace("APPSECRET", APP_SECRET))
            val request = HttpRequest.newBuilder()
                    .GET()
                    .uri(uri)
                    .timeout(Duration.ofMillis(50000))
                    .build()
            val response = client.send(request, HttpResponse.BodyHandlers.ofString()).body()
            println(response)
            ACCESS_TOKEN_AND_TICKET["access_token"] = Gson().fromJson<MutableMap<String, Any>>(response, MutableMap::class.java)["access_token"] as String
        }
        kotlin.run {
            val uri = URI.create(JS_API_TICKET_URL.replace("ACCESS_TOKEN", ACCESS_TOKEN_AND_TICKET["access_token"] as String))
            val request = HttpRequest.newBuilder()
                    .GET()
                    .uri(uri)
                    .timeout(Duration.ofMillis(50000))
                    .build()
            val response = client.send(request, HttpResponse.BodyHandlers.ofString()).body()
            println(response)
            ACCESS_TOKEN_AND_TICKET["jsapi_ticket"] = Gson().fromJson<MutableMap<String, Any>>(response, MutableMap::class.java)["ticket"] as String
        }
    }
}