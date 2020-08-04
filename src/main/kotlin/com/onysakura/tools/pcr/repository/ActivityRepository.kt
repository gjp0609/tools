package com.onysakura.tools.pcr.repository

import com.onysakura.tools.pcr.model.Activity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface ActivityRepository : JpaRepository<Activity, Long> {
    fun findAllByBeginDateBeforeAndEndDateAfter(beginDate: Date, endDate: Date): MutableList<Activity>
}