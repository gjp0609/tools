package com.onysakura.tools.pcr.model

class Constants {
    enum class Type {
        T, M, S
    }

    enum class Round(val int: Int) {
        ONE(1), TWO(2), UNKNOWN(0)
    }

    enum class Stars(val int: Int) {
        ONE(1), TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6), UNKNOWN(0)
    }

    companion object {
        init {

        }
    }
}