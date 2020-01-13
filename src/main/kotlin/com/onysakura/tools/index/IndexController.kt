package com.onysakura.tools.index

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody

@Controller
@RequestMapping
class IndexController {

    @GetMapping("/.well-known/acme-challenge/Zztzk9KC0NZBX4CHmYkBOEvp0McwZhXtnBYr-HddJbY")
    @ResponseBody
    fun test(): String {
        return "Zztzk9KC0NZBX4CHmYkBOEvp0McwZhXtnBYr-HddJbY.0S68Vf0MxnXTpNvycJ5Ps8HSj3BapI1vlVStsBeZg2k"
    }

    @GetMapping
    fun index(): String {
        return "index"
    }

    @GetMapping("/{target}")
    fun page(@PathVariable("target") target: String): String {
        return "forward:/pages/$target.html"
    }

}