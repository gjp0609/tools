package com.onysakura.tools.index

import org.springframework.data.jpa.repository.JpaRepository

interface PoetRepository : JpaRepository<Poet, Long> {

}