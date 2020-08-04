package com.onysakura.tools.pcr.repository

import com.onysakura.tools.pcr.model.Princess
import org.springframework.data.jpa.repository.JpaRepository

interface PrincessRepository : JpaRepository<Princess, Long> {
}