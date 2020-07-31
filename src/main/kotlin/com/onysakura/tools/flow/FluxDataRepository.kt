//package com.onysakura.tools.flow
//
//import org.springframework.context.annotation.Profile
//import org.springframework.data.mongodb.repository.ReactiveMongoRepository
//import reactor.core.publisher.Flux
//
//@Profile("dev")
//interface FluxDataRepository : ReactiveMongoRepository<Data, String> {
//
//    /**
//     * get all data by title
//     */
//    fun getAllByTitle(title: String): Flux<Data>
//}