package com.onysakura.tools.qrcode

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/qrcode")
class QrCodeController {

    @GetMapping("/generate")
    fun generate(): Any {
        return QrCode("http://me","sad")
    }
}
