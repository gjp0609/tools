package com.onysakura.tools.test

import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@CrossOrigin(origins = ["onysakura.com", "https://onysakura.xyz"])
@RequestMapping("/test")
class TestController {

    @GetMapping("/echo")
    fun test(s: String): String {
        return s
    }

    @GetMapping("/get")
    fun get() {

    }
}