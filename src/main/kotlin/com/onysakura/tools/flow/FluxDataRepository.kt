package com.onysakura.tools.flow

import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Flux

interface FluxDataRepository : ReactiveMongoRepository<Data, String> {

    /**
     * get all data by title
     */
    fun getAllByTitle(title: String): Flux<Data>
}