package com.onysakura.tools.index

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping
class IndexController {

    @GetMapping
    fun index(): String {
        return "index"
    }

    @GetMapping("/{target}")
    fun page(@PathVariable("target") target: String): String {
        return target
    }

}
//    @GetMapping
//    @ResponseBody
//    fun wechat(echostr: String): String {
//        return echostr
//    }

//    val OAUTH2_URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECTURI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect"
//    val appId = "wx6840b5bb18a51971"
//
//    @GetMapping("/getCode")
//    fun redirect(url: String, scope: String, response: HttpServletResponse) {
//        println(url)
//        val encodeUrl = encode(url, StandardCharsets.UTF_8)
//        val redirectUrl: String = OAUTH2_URL
//                .replace("APPID", appId)
//                .replace("REDIRECTURI", encode("http://tools.onysakura.com/redirectBack?url=$encodeUrl", StandardCharsets.UTF_8))
//                .replace("SCOPE", scope)
//        response.sendRedirect(redirectUrl)
//    }
//
//    @GetMapping("/redirectBack")
//    @Throws(Exception::class)
//    fun redirectBack(url: String, response: HttpServletResponse, code: String) {
//        val redirectUrl = url + (if (url.contains("?")) "&" else "?") + "code=$code"
//        response.sendRedirect(redirectUrl)
//    }