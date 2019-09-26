package com.onysakura.tools.test;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {
    public String hello(String name) {
        return "hello " + name;
    }
}
