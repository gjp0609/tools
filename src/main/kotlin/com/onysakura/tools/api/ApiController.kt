package com.onysakura.tools.api

import com.onysakura.tools.utils.StringUtils
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletResponse

@CrossOrigin(origins = ["onysakura.com", "*.onysakura.com", "https://onysakura.fun"])
@RestController
@RequestMapping("/api")
open class ApiController(private val repository: ApiRepository) {

    @PutMapping
    fun put(@RequestBody content: String): String {
        var randomStr: String = StringUtils.randomStr(4)
        var count = 0
        while (repository.existsById(randomStr)) {
            randomStr = StringUtils.randomStr(4)
            count++
        }
        if (count > 10) {
            val warn = Api()
            warn.code = StringUtils.randomStr(10)
            warn.content = count.toString()
            repository.save(warn)
        }
        val api = Api()
        api.code = randomStr
        api.content = content
        val save: Api = repository.save(api)
        return save.code
    }

    @GetMapping("/{code}")
    fun get(@PathVariable("code") code: String, response: HttpServletResponse) {
        repository.findById(code).ifPresent {
            response.writer.print(it.content)
        }
    }

    @GetMapping("/r/{code}")
    fun redirect(@PathVariable(value = "code", required = true) code: String, response: HttpServletResponse) {
        repository.findById(code).ifPresent {
            response.sendRedirect(it.content)
        }
    }

}