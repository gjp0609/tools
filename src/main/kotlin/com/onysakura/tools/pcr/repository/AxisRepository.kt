package com.onysakura.tools.pcr.repository

import com.onysakura.tools.pcr.model.Axis
import org.springframework.data.jpa.repository.JpaRepository

interface AxisRepository : JpaRepository<Axis, Long> {
}