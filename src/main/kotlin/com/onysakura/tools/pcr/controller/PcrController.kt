package com.onysakura.tools.pcr.controller

import com.onysakura.tools.common.Resp
import com.onysakura.tools.common.ServiceException
import com.onysakura.tools.miniprogram.MiniProgramUtils
import com.onysakura.tools.pcr.model.Axis
import com.onysakura.tools.pcr.repository.*
import com.onysakura.tools.utils.DateUtils
import com.onysakura.tools.utils.MoshiUtils
import com.onysakura.tools.utils.StringUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.util.*
import javax.servlet.http.HttpServletRequest

@CrossOrigin(origins = ["https://pages.onysakura.com", "https://onysakura.fun"])
@RestController
@RequestMapping("/pcr")
open class PcrController(
        private val princessRepository: PrincessRepository,
        private val axisRepository: AxisRepository,
        private val jdbcTemplate: JdbcTemplate,
        private val bossRepository: BossRepository,
        private val activityRepository: ActivityRepository) {

    val log: Logger = LoggerFactory.getLogger(PcrController::class.java)

    companion object {
        fun isNotLogin(request: HttpServletRequest): Boolean {
            return request.session.getAttribute("sessionKey") == null
        }
    }

    @Value(value = "\${custom.uploadPath}")
    private val uploadPath: String = ""

    @GetMapping("/login")
    fun login(code: String, request: HttpServletRequest): Resp<*> {
        log.info("code: $code")
        val sessionKey: String = MiniProgramUtils.codeToSession(code)
        log.info("sessionKey: $sessionKey")
        request.session.setAttribute("sessionKey", sessionKey)
        return Resp(request.session.id)
    }

    @GetMapping("/princess")
    fun princessList(): Resp<*> {
        val list = princessRepository.findAll()
        log.info("list: $list")
        return Resp(list)
    }

    @GetMapping("/activity")
    fun activityList(): Resp<*> {
        val list = activityRepository.findAll()
        log.info("list: $list")
        return Resp(list)
    }


    @GetMapping("/activity/current")
    fun currentActivity(): Resp<*> {
        val now = Date()
        val list = activityRepository.findAllByBeginDateBeforeAndEndDateAfter(now, now)
        log.info("list: $list")
        if (list.size > 0) {
            return Resp(list[0].id)
        } else {
            return Resp.error()
        }
    }

    @GetMapping("/boss")
    fun bossList(activityId: Long): Resp<*> {
        val list = bossRepository.findAllByActivityId(activityId)
        log.info("boss list: $list")
        return Resp(list)
    }

    @GetMapping("/axis")
    fun axisList(
            @RequestParam(value = "activityId", required = false, defaultValue = "0") activityId: Long,
            @RequestParam(value = "bossId", required = false, defaultValue = "0") bossId: Long,
            @RequestParam(value = "princessList", required = false, defaultValue = "") princessList: String,
            @RequestParam(value = "invertSelection", required = false, defaultValue = "false") invertSelection: Boolean
    ): Resp<*> {
        var sql = "select a.* from pcr_axis a left join pcr_boss b on a.boss_id = b.id where status in (0,1) "
        if (activityId != 0L) {
            sql += "and b.activity_id = $activityId "
        }
        if (bossId != 0L) {
            sql += "and a.boss_id = $bossId "
        }
        if (princessList.isNotBlank()) {
            val list: MutableList<String>? = MoshiUtils.listFromJson(princessList)
            list?.forEach {
                sql += "and ${if (invertSelection) "not" else ""} find_in_set('$it', a.princess_str) "
            }
        }
        sql += ";"
        log.info("sql: $sql")
        val list: MutableList<Axis> = jdbcTemplate.query(sql, JdbcRowMapperWrapper(Axis::class.java))
        log.info("list: $list")
        return Resp(list)
    }

    @PutMapping("/axis")
    fun addAxis(@RequestBody axis: Axis, request: HttpServletRequest): Resp<*> {
        if (isNotLogin(request)) {
            throw ServiceException("Use MiniProgram To Login")
        }
        log.info("add axis: $axis")
        axisRepository.save(axis)
        return Resp.success()
    }

    @PostMapping(value = ["/upload"], consumes = ["multipart/form-data"], produces = ["application/json"])
    fun upload(@RequestParam("file") file: MultipartFile, request: HttpServletRequest): Resp<*> {
        if (isNotLogin(request)) {
            throw ServiceException("Use MiniProgram To Login")
        }
        log.info("add axis: $file")
        val dir = File("$uploadPath/pcr/axis/")
        if (!dir.exists()) {
            dir.mkdirs()
        }
        val originalFilename: String = file.originalFilename ?: "null.jpg"
        val name: String = DateUtils.nowStr() + "-" + StringUtils.randomStr(4) + originalFilename.substring(originalFilename.lastIndexOf("."))
        file.inputStream.transferTo(File("$uploadPath/pcr/axis/$name").outputStream())
        return Resp(name)
    }
}