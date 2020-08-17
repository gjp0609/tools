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
    }
}