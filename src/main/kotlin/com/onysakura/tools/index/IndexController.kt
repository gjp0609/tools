package com.onysakura.tools.index

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody

@Controller
@RequestMapping
class IndexController {

    @GetMapping
    fun index(): String {
        return "index"
    }

    @GetMapping("/{target}")
    fun page(@PathVariable("target") target: String): String {
        return "forward:/pages/$target.html"
    }

    @GetMapping("/.well-known/pki-validation/fileauth.txt")
    @ResponseBody
    fun auth():String{
        return "202001130144331hgax0uxp9a3cx8dn9c240w23ibdj2q1u3i8ts7c0ws65xe436"
    }
}