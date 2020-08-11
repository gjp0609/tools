package com.onysakura.tools.pcr.controller

import com.onysakura.tools.pcr.model.Activity
import com.onysakura.tools.pcr.model.Axis
import com.onysakura.tools.pcr.model.Boss
import com.onysakura.tools.pcr.model.Princess
import com.onysakura.tools.pcr.repository.ActivityRepository
import com.onysakura.tools.pcr.repository.AxisRepository
import com.onysakura.tools.pcr.repository.BossRepository
import com.onysakura.tools.pcr.repository.PrincessRepository
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Sort
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@CrossOrigin(origins = ["onysakura.com", "*.onysakura.com", "https://onysakura.xyz"])
@RestController
@RequestMapping("/pcr")
open class PcrController(
        private val princessRepository: PrincessRepository,
        private val axisRepository: AxisRepository,
        private val bossRepository: BossRepository,
        private val activityRepository: ActivityRepository) {

    val log = LoggerFactory.getLogger(PcrController::class.java)

    @GetMapping("/princess")
    fun princessList(): MutableList<Princess> {
        val list = princessRepository.findAll()
        log.info("list: $list")
        return list
    }

    @GetMapping("/activity")
    fun activityList(): MutableList<Activity> {
        val list = activityRepository.findAll()
        log.info("list: $list")
        return list
    }


    @GetMapping("/activity/current")
    fun currentActivity(): Long {
        val now = Date()
        val list = activityRepository.findAllByBeginDateBeforeAndEndDateAfter(now, now)
        log.info("list: $list")
        if (list.size > 0) {
            return list[0].id
        } else {
            return 0L
        }
    }


    @GetMapping("/boss")
    fun bossList(activityId: Long): MutableList<Boss> {
        val list = bossRepository.findAllByActivityId(activityId)
        log.info("boss list: $list")
        return list
    }

    @GetMapping("/axis")
    fun axisList(activityId: Long, bossId: Long): MutableList<Axis> {
        val list: MutableList<Axis> = axisRepository.findAll(Sort.by(Sort.Order.desc("createTime")))
        log.info("list: $list")
        return list
    }
}