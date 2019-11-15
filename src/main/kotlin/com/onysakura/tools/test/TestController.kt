package com.onysakura.tools.test;

import org.springframework.http.codec.ServerSentEvent
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.util.function.Tuples
import java.time.Duration
import java.util.*
import java.util.concurrent.ThreadLocalRandom

@RestController
@RequestMapping("/test")
class TestController(private val testRepository: TestRepository) {

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

    val STR_SIZE = 131072
    val TRIES = 8192

    val enc = Base64.getEncoder()
    val dec = Base64.getDecoder()

    @Scheduled(cron = "0 * * * * ?")
    fun task() {
        val start = System.nanoTime()
        test()
        testRepository.save(Test(System.nanoTime() - start))
    }

    fun test() {
        val str = "a".repeat(STR_SIZE).toByteArray()

        println("JIT warming up")
        repeat(5) {
            dec.decode(enc.encodeToString(str))
        }

        var encStr = enc.encodeToString(str)
        print("encode ${String(str.sliceArray(1..4))}... to ${encStr.substring(0, 4)}...: ")

        var count1 = 0
        val t1 = System.nanoTime()
        repeat(TRIES) {
            encStr = enc.encodeToString(str)
            count1 += encStr.length
        }
        println("${count1}, ${(System.nanoTime() - t1) / 1e9}")

        var decBytes = dec.decode(encStr)
        print("decode ${encStr.substring(0, 4)}... to ${String(decBytes.sliceArray(1..4))}...: ")
        var count2 = 0

        val t2 = System.nanoTime()
        repeat(TRIES) {
            decBytes = dec.decode(encStr)
            count2 += decBytes.size
        }
        val now = System.nanoTime()
        println("${count2}, ${(now - t2) / 1e9}")
        println("overall time: ${(now - t1) / 1e9}s")
    }
}