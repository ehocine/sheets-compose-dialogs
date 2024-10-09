package com.maxkeppeler.sheets.clock.utils

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

internal fun LocalTime.Companion.now(): LocalTime {
    return Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).time
}

internal val Char.numericValue: Int
    get() {
        if (this.isDigit()) {
            return this.digitToInt()
        }
        val codePoint = this.code

        if (codePoint < 128) {
            if (codePoint >= '0'.code && codePoint <= '9'.code) {
                return codePoint - '0'.code
            }
            if (codePoint >= 'a'.code && codePoint <= 'z'.code) {
                return codePoint - ('a'.code - 10)
            }
            if (codePoint >= 'A'.code && codePoint <= 'Z'.code) {
                return codePoint - ('A'.code - 10)
            }
            return codePoint
        }
        // Full-width uppercase A-Z.
        if (codePoint in 0xff21..0xff3a) {
            return codePoint - 0xff17
        }
        // Full-width lowercase a-z.
        if (codePoint in 0xff41..0xff5a) {
            return codePoint - 0xff37
        }
        return codePoint
    }