package com.onysakura.tools.kindle

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.RequestMapping

@Component
@RequestMapping
class Job {

//    @Scheduled(cron = "0 * * * * ?")
    fun job() {
        print("wqe")
    }
}