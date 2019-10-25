package com.onysakura.tools.url

import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UrlRepository : JpaRepository<UrlEntity, String> {
    fun findByUrl(url: String): Optional<UrlEntity>
}