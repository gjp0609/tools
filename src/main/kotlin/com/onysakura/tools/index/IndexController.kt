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

    @GetMapping("/page/{target}")
    fun index(@PathVariable("target") target: String): String {
        return target
    }
}