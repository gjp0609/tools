package com.onysakura.tools.miniprogram

import com.onysakura.tools.common.ServiceException
import com.onysakura.tools.utils.MoshiUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.util.StringUtils
import java.net.URL

class MiniProgramUtils {

    companion object {
        val log: Logger = LoggerFactory.getLogger(MiniProgramUtils::class.java)
        const val APP_ID = "APP_ID_TO_REPLACE"
        const val APP_SECRET = "APP_SECRET_TO_REPLACE"
        const val CODE_TO_SESSION_URL = "https://api.weixin.qq.com/sns/jscode2session?appid=APPID&secret=SECRET&js_code=JSCODE&grant_type=authorization_code"

        fun codeToSession(code: String): String {
            val url = CODE_TO_SESSION_URL
                    .replace("APPID", APP_ID)
                    .replace("SECRET", APP_SECRET)
                    .replace("JSCODE", code)
            log.info("url: $url")
            val resp = URL(url).readText()
            log.info("resp: $resp")
            if (!StringUtils.isEmpty(resp)) {
                val json: MutableMap<String, String>? = MoshiUtils.mapFromJson(resp)
                if (!json.isNullOrEmpty()) {
                    if (json.containsKey("session_key")) {
                        val sessionKey: String? = json["session_key"]
                        if (!sessionKey.isNullOrBlank()) {
                            return sessionKey
                        }
                    }
                }
            }
            throw ServiceException("get sessionKey error: $resp")
        }
    }
}