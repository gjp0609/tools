package com.onysakura.tools.test

import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@CrossOrigin(origins = ["http://localhost", "http://onysakura.xyz"])
@RequestMapping("/test")
class TestController {

    @GetMapping("/get")
    fun test(s: String): String {
        return s
    }
}