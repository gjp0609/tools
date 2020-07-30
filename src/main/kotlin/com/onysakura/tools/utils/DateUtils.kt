package com.onysakura.tools.utils

import java.text.SimpleDateFormat
import java.util.*

class DateUtils {

    companion object {

        val YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss"
        val YYYYMMDDHHMMSS = "yyyyMMddHHmmss"

        fun format(date: Date?, patten: String?): String? {
            val format = SimpleDateFormat(patten)
            return format.format(date)
        }
    }
}