package com.onysakura.tools.utils

import kotlin.random.Random

class StringUtils {

    companion object {
        val random: Random.Default = Random.Default
        const val seed: String = "" +
                "01234567890" +
                "abcdefg" +
                "hijklmn" +
                "opgrst" +
                "uvwxyz"

        fun randomStr(length: Int): String {
            val stringBuilder = StringBuilder()
            for (i: Int in 0..length) {
                val int: Int = random.nextInt(seed.length)
                stringBuilder.append(seed[int])
            }
            return stringBuilder.toString()
        }

        /**
         * 驼峰转下划线
         */
        fun humpToUnderline(para: String): String? {
            val sb = StringBuilder(para)
            var temp = 0
            if (!para.contains("_")) {
                for (i: Int in para.indices) {
                    if (Character.isUpperCase(para[i])) {
                        sb.insert(i + temp, "_")
                        temp += 1
                    }
                }
            }
            return sb.toString().toUpperCase()
        }

        /**
         * 下划线转驼峰
         */
        fun underlineToHump(para: String): String? {
            val result = StringBuilder()
            val a: Array<String> = para.split("_").toTypedArray()
            for (s: String in a) {
                if (!para.contains("_")) {
                    result.append(s.substring(0, 1).toUpperCase())
                    result.append(s.substring(1).toLowerCase())
                    continue
                }
                if (result.isEmpty()) {
                    result.append(s.toLowerCase())
                } else {
                    result.append(s.substring(0, 1).toUpperCase())
                    result.append(s.substring(1).toLowerCase())
                }
            }
            return result.toString()
        }
    }
}