package com.onysakura.tools.test

import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin(origins = ["onysakura.com", "https://onysakura.xyz"])
@RequestMapping("/test")
class TestController {

    @GetMapping("/echo")
    fun test(@RequestBody map: MutableMap<String, String>): String {
        println(map)
        return "s"
    }

    @GetMapping("/get")
    fun get() {

    }
}