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
        return "forward:/pages/$target.html"
    }
}