package com.onysakura.tools.utils

import kotlin.random.Random

class StringUtils {

    companion object {
        val random: Random.Default = Random.Default
        val seed = "" +
                "01234567890" +
                "abcdefg" +
                "hijklmn" +
                "opgrst" +
                "uvwxyz"

        fun randomStr(length: Int): String {
            val int = random.nextInt(seed.length)
            val stringBuilder = StringBuilder()
            for (i: Int in 0..length) {
                stringBuilder.append(seed[int])
            }
            return stringBuilder.toString()
        }
    }
}