package com.onysakura.tools.index

import com.google.gson.Gson
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import java.util.*


@Controller
@RequestMapping
class IndexController(private val poetRepository: PoetRepository) {
    private val max = 311828
    val random = Random(System.currentTimeMillis())
    @GetMapping
    fun index(model: Model): String {
        val randomIndex = random.nextInt(max) + 1
        val optionalPoet = poetRepository.findById(randomIndex.toLong())
        optionalPoet.ifPresent {
            model.addAttribute("title", it.title)
            model.addAttribute("author", it.author)
            val list = Gson().fromJson<MutableList<String>>(it.paragraphs, MutableList::class.java)
            list.shuffle(random)
            model.addAttribute("paragraph", list[0])
        }
        return "index"
    }

    @GetMapping("/page/{target}")
    fun index(@PathVariable("target") target: String): String {
        return target
    }

    @GetMapping("/poetry")
    @ResponseBody
    fun poetry(): Poet {
        val randomIndex = random.nextInt(max) + 1
        val optionalPoet = poetRepository.findById(randomIndex.toLong())
        if (optionalPoet.isPresent) {
            return optionalPoet.get()
        }
        return Poet()
    }
}