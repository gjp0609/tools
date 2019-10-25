package com.onysakura.tools.test;

import org.springframework.http.codec.ServerSentEvent
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.util.function.Tuples
import java.time.Duration
import java.util.concurrent.ThreadLocalRandom

@RestController
@RequestMapping("/test")
class TestController {

    @GetMapping("/randomNumbers")
    fun randomNumbers(): Flux<ServerSentEvent<Int>> {
        return Flux.interval(Duration.ofSeconds(1))
                .map { seq -> Tuples.of(seq!!, ThreadLocalRandom.current().nextInt()) }
                .map { data ->
                    ServerSentEvent.builder<Int>()
                            .event("random")
                            .id(data.t1.toString())
                            .data(data.t2)
                            .build()
                }
    }
}