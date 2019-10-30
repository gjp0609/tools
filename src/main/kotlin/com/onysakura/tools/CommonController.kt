package com.onysakura.tools;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/page")
class CommonController {

    @GetMapping("/{page}")
    fun page(@PathVariable("page") page: String): String {
        return page.replace("-", "/");
    }
}