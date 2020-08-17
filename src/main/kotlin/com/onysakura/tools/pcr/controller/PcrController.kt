package com.onysakura.tools.pcr.controller

import com.onysakura.tools.pcr.model.Activity
import com.onysakura.tools.pcr.model.Axis
import com.onysakura.tools.pcr.model.Boss
import com.onysakura.tools.pcr.model.Princess
import com.onysakura.tools.pcr.repository.ActivityRepository
import com.onysakura.tools.pcr.repository.AxisRepository
import com.onysakura.tools.pcr.repository.BossRepository
import com.onysakura.tools.pcr.repository.PrincessRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Sort
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.util.*

@CrossOrigin(origins = ["onysakura.com", "*.onysakura.com", "https://onysakura.xyz"])
@RestController
@RequestMapping("/pcr")
open class PcrController(
        private val princessRepository: PrincessRepository,
        private val axisRepository: AxisRepository,
        private val bossRepository: BossRepository,
        private val activityRepository: ActivityRepository) {

    val log: Logger = LoggerFactory.getLogger(PcrController::class.java)

    @Value(value = "\${custom.uploadPath}")
    private val uploadPath: String = ""

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

    @PostMapping(value = ["/upload"], produces = ["multipart/form-data"])
    fun upload(@RequestParam("file") file: MultipartFile) {
        log.info("add axis: $file")
        val f = File(uploadPath + "/pcr/axis/" + file.originalFilename)
        file.inputStream.transferTo(f.outputStream())
    }

    @PutMapping("/axis")
    fun addAxis(@RequestBody axis: Axis) {
        log.info("add axis: $axis")
        axisRepository.save(axis)
    }
}