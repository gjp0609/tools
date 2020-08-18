package com.onysakura.tools.api

import org.springframework.data.jpa.repository.JpaRepository

interface ApiRepository : JpaRepository<Api, String> {
}