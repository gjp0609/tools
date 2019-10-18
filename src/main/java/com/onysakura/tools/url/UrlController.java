package com.onysakura.tools.url;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;
import java.util.Random;

@Controller
@RequestMapping("/t")
public class UrlController {

    private static final int CODE_LENGTH = 5;
    private final UrlRepository urlRepository;

    @Autowired
    public UrlController(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    @GetMapping("/{code}")
    public void redirect(@PathVariable("code") String code, HttpServletResponse response) throws IOException {
        Optional<UrlEntity> urlEntityOptional = urlRepository.findById(code);
        if (urlEntityOptional.isPresent()) {
            response.sendRedirect(urlEntityOptional.get().getUrl());
        } else {
            response.getWriter().println("Url Not Found !");
        }
    }

    @GetMapping("/get/{code}")
    @ResponseBody
    public String get(@PathVariable("code") String code) throws IOException {
        Optional<UrlEntity> urlEntityOptional = urlRepository.findById(code);
        if (urlEntityOptional.isPresent()) {
            return urlEntityOptional.get().getUrl();
        } else {
            return "Url Not Found !";
        }
    }

    @PostMapping
    @ResponseBody
    public String setCode(@RequestBody HashMap<String, String> map) {
        String url = map.get("url");
        if (url == null) {
            return "";
        }
        Optional<UrlEntity> urlEntityOptional = urlRepository.findByUrl(url);
        if (urlEntityOptional.isPresent()) {
            return urlEntityOptional.get().getCode();
        }
        String code;
        do {
            code = getRandomString();
        } while (urlRepository.existsById(code));
        UrlEntity urlEntity = new UrlEntity(code, url);
        urlEntity = urlRepository.save(urlEntity);
        return urlEntity.getCode();
    }

    /**
     * 随机生成字符串
     */
    private static String getRandomString() {
        char[] charArray = ("1234567890" +
                "abcdefghijklmnopqrstuvwxyz" +
                "ABCDEFGHIJKLMNOPQRSTUVWXYZ").toCharArray();
        StringBuilder sb = new StringBuilder();
        Random r = new Random();
        for (int x = 0; x < CODE_LENGTH; ++x) {
            sb.append(charArray[r.nextInt(charArray.length)]);
        }
        return sb.toString();
    }
}
