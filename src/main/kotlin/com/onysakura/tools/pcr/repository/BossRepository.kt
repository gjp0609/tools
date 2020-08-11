package com.onysakura.tools.pcr.repository

import com.onysakura.tools.pcr.model.Boss
import org.springframework.data.jpa.repository.JpaRepository

interface BossRepository : JpaRepository<Boss, Long> {
    fun findAllByActivityId(activityId: Long): MutableList<Boss>
}