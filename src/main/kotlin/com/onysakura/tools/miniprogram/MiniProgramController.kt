package com.onysakura.tools.miniprogram

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*
import java.nio.charset.StandardCharsets
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/miniprogram")
class MiniProgramController {
    val log = LoggerFactory.getLogger(MiniProgramController::class.java)

    @GetMapping(value = ["/"])
    fun urlAuth(echostr: String): String {
        return echostr
    }

    @PostMapping(value = ["/decrytUserInfo"])
    fun decrytUserInfo(@RequestBody map: MutableMap<String, Any>): String {
        log.info("map:$map")
        return "sad"
    }

    @PostMapping(value = ["/"], consumes = ["text/xml", "application/xml", "application/json"])
    fun message(@RequestBody requestMap: MutableMap<String, Any>, response: HttpServletResponse) {
        log.info("MiniProgram Message -> $requestMap")
        when (requestMap["MsgType"]) {
            "event" -> {
                val event = requestMap["Event"] as String
                log.info("MiniProgram Event: $event")
            }
            "text" -> {
                val responseMap = mutableMapOf<String, Any>()
                responseMap["ToUserName"] = requestMap["FromUserName"] as String
                responseMap["FromUserName"] = requestMap["ToUserName"] as String
                responseMap["CreateTime"] = System.currentTimeMillis()
                responseMap["MsgType"] = "text"
                responseMap["Content"] = (requestMap["Content"] as String)
                        .replace("吗", "")
                        .replace("?", ".")
                        .replace("？", "。")
                val xmlMapper = XmlMapper()
                xmlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                xmlMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false)
                xmlMapper.enable(MapperFeature.USE_STD_BEAN_NAMING)
                xmlMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
                xmlMapper.propertyNamingStrategy = PropertyNamingStrategy.UPPER_CAMEL_CASE
                response.characterEncoding = StandardCharsets.UTF_8.toString()
                response.contentType = "text/plain; charset=UTF-8"
                response.writer.println(xmlMapper.writerWithDefaultPrettyPrinter().withRootName("xml").writeValueAsString(responseMap))
            }
        }
    }
}