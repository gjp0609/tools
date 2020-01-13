package com.onysakura.tools.wechat

import com.google.gson.Gson
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import java.net.URI
import java.net.URLEncoder
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.nio.charset.StandardCharsets
import java.time.Duration
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Controller
@RequestMapping("/wechat")
class WechatController {

    companion object {
        const val AUTH_URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECTURI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect"
        const val APP_ID = "xxxxxxxxxx"
        const val APP_SECRET = "xxxxxxxxxxxx"
        const val REDIRECT_URL = "xxxxxxxxxxxxx"
        const val ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET"
        const val JS_API_TICKET_URL = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi"
        val ACCESS_TOKEN_AND_TICKET = mutableMapOf<String, String>()
    }

    @GetMapping
    @ResponseBody
    fun echo(echostr: String): String {
        return echostr
    }

    @GetMapping("/getCode")
    fun redirect(url: String, scope: String, response: HttpServletResponse) {
        println(url)
        val encodeUrl = URLEncoder.encode(url, StandardCharsets.UTF_8)
        response.sendRedirect(AUTH_URL
                .replace("APPID", APP_ID)
                .replace("REDIRECTURI", URLEncoder.encode("$REDIRECT_URL?url=$encodeUrl", StandardCharsets.UTF_8))
                .replace("SCOPE", scope))
    }

    @GetMapping("/redirectBack")
    fun redirectBack(url: String, response: HttpServletResponse, code: String) {
        val redirectUrl = url + (if (url.contains("?")) "&" else "?") + "code=$code"
        response.sendRedirect(redirectUrl)
    }

    @GetMapping("/getTokenAndTicket")
    @ResponseBody
    fun getTokenAndTicket(): MutableMap<String, Any> {
        val mutableMap = mutableMapOf<String, Any>()
        mutableMap["timestamp"] = System.currentTimeMillis()
        mutableMap["access_token"] = ACCESS_TOKEN_AND_TICKET["access_token"] as String
        mutableMap["jsapi_ticket"] = ACCESS_TOKEN_AND_TICKET["jsapi_ticket"] as String
        return mutableMap
    }

    @Scheduled(fixedRate = 1000 * 60 * 60 * 2) // 2 hours
    fun getTokenAndTicketJob() {
        val client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofMillis(5000))
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build()
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