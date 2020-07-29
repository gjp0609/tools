package com.onysakura.tools.flow

import org.springframework.context.annotation.Profile
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.Duration
import java.util.*

@Profile("dev")
@RestController
@RequestMapping("/flow")
class FlowController(private val dataRepository: FluxDataRepository) {

    @GetMapping("/save")
    fun save(s: String): Mono<Data> {
        val data = Data()
        data.id = UUID.randomUUID().toString()
        data.time = Date()
        data.title = s
        return dataRepository.save(data)
    }

    @GetMapping("/get")
    fun get(): Flux<Data> {
        return Flux.interval(Duration.ofMillis(1000)).flatMap { return@flatMap dataRepository.findAll() }
    }
}