package com.onysakura.tools.url

import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView
import java.util.*
import javax.servlet.http.HttpServletResponse

@Controller
@RequestMapping("")
class UrlController(private val urlRepository: UrlRepository) {

    private val codeLength = 4

    @GetMapping("/t/{code}")
    fun redirect(@PathVariable("code") code: String, response: HttpServletResponse) {
        val urlEntityOptional = urlRepository.findById(code)
        if (urlEntityOptional.isPresent) {
            response.sendRedirect(urlEntityOptional.get().url)
        } else {
            response.writer.println("Url Not Found !")
        }
    }

    @GetMapping("/text/{code}")
    @ResponseBody
    fun text(@PathVariable("code") code: String): String {
        val urlEntityOptional = urlRepository.findById(code)
        var text = "Not Found !"
        if (urlEntityOptional.isPresent) {
            text = urlEntityOptional.get().url
        }
        return text
    }

    @PostMapping("/t")
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