package com.onysakura.tools.url

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.servlet.http.HttpServletResponse


@Controller
@RequestMapping("/t")
class UrlController(private val urlRepository: UrlRepository) {

    private val codeLength = 5

    @GetMapping("/{code}")
    fun redirect(@PathVariable("code") code: String, response: HttpServletResponse) {
        val urlEntityOptional = urlRepository.findById(code)
        if (urlEntityOptional.isPresent) {
            response.sendRedirect(urlEntityOptional.get().url)
        } else {
            response.writer.println("Url Not Found !")
        }
    }

    @GetMapping("/get/{code}")
    fun getText(@PathVariable("code") code: String, response: HttpServletResponse) {
        val urlEntityOptional = urlRepository.findById(code)
        if (urlEntityOptional.isPresent) {
            response.writer.println(urlEntityOptional.get().url)
        } else {
            response.writer.println("Text Not Found !")
        }
    }

    @GetMapping("/get/{code}")
    @ResponseBody
    operator fun get(@PathVariable("code") code: String): String {
        val urlEntityOptional = urlRepository.findById(code)
        return if (urlEntityOptional.isPresent) {
            urlEntityOptional.get().url
        } else {
            "Url Not Found !"
        }
    }

    @PostMapping
    @ResponseBody
    fun setCode(@RequestBody map: HashMap<String, String>): String {
        val url = map["url"] ?: return ""
        val urlEntityOptional = urlRepository.findByUrl(url)
        if (urlEntityOptional.isPresent) {
            return urlEntityOptional.get().code
        }
        var code: String
        do {
            code = getRandomString()
        } while (urlRepository.existsById(code))
        var urlEntity = UrlEntity(code, url)
        urlEntity = urlRepository.save(urlEntity)
        return urlEntity.code
    }

    /**
     * 随机生成字符串
     */
    private fun getRandomString(): String {
        val charArray = ("1234567890" +
                "abcdefghijklmnopqrstuvwxyz" +
                "ABCDEFGHIJKLMNOPQRSTUVWXYZ").toCharArray()
        val sb = StringBuilder()
        val r = Random()
        for (x in 0 until codeLength) {
            sb.append(charArray[r.nextInt(charArray.size)])
        }
        return sb.toString()
    }
}