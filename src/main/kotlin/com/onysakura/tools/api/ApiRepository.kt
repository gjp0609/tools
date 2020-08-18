package com.onysakura.tools.api

import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface ApiRepository : JpaRepository<Api, String> {
    fun findFirstByContent(content: String): Optional<Api>
}