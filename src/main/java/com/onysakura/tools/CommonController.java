package com.onysakura.tools;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/page")
public class CommonController {

    @GetMapping("/{page}")
    public String main(@PathVariable("page") String page) {
        return page.replace("-", "/");
    }
}
