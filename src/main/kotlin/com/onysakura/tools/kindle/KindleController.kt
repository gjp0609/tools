package com.onysakura.tools.kindle

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/kindle")
class KindleController {

    @GetMapping
    fun generate(model: Model): String {
        model.addAttribute("title", "GitHub开源编辑器Atom")
        model.addAttribute("text", "    <div class=\"post_content\" id=\"paragraph\">\n" +
                "        <p>11月27日下午消息，在2019WISE大会上，蜻蜓FM创始人兼CEO张强在演讲中宣布，经过一年的耕耘，蜻蜓FM全场景生态月活跃用户数达到1.3亿，全年增长近30%，智能设备单日收听总时长达2500万小时。</p>\n" +
                "        <p><img src=\"https://n.sinaimg.cn/tech/transform/116/w550h366/20191127/b2db-iixntzz6758439.jpg\" class=\"lazy\"\n" +
                "                title=\"蜻蜓FM发布生态数据，月活达到1.3亿\" referrerpolicy=\"no-referrer\"></p>\n" +
                "        <p>▲蜻蜓FM创始人兼CEO张强</p>\n" +
                "        <p>张强表示，万物互联的时代，音频面临着巨大的机会，更加精细的场景划分推动音频的渗透和发展，而如何做好音频内容与场景的匹配，也是一项挑战。</p>\n" +
                "        <p>\n" +
                "            “我们认为未来AIoT时代——万物互联时代对音频是一个巨大的机会，音频的渗透率将会有一个非常大的提升。”他表示，现在音频的整体渗透率比视频、文字低了不少，大概是在30%左右，后续随着5G的来临会出现很多新的终端，很多硬件终端都会被接入物联网。在新的这些终端上，人机交互会发生一些新的变化，比如语音交互会越来越被普及，而音频的机会就会大过在视觉交互情况下的机会，“这是我们对音频在未来时代的判断”。</p>\n" +
                "        <p>张强提到，在AIoT时代，对于一个具体用户来说，我们怎样把TA的诉求同硬件、场景和内容匹配起来？这就是蜻蜓FM音频全场景生态要解决的。</p>\n" +
                "        <p>他透露，蜻蜓现在已经和全国差不多600家企业、硬件或者AI等等厂商进行合作，已经有9000万台智能设备植入蜻蜓的内容。</p>\n" +
                "    </div>")
        return "article"
    }
}
