package com.maxkeppeler.sheets.clock.views

import com.maxkeppeler.sheets.clock.utils.FormatStyle
import kotlinx.datetime.LocalTime
import kotlinx.datetime.format.DateTimeFormat
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern

@OptIn(FormatStringsInDatetimeFormats::class)
internal actual fun getTimeFormatter(style: FormatStyle): DateTimeFormat<LocalTime> {
    val pattern = when (style) {
        FormatStyle.FULL -> "HH:mm:ss zzz"
        FormatStyle.LONG -> "HH:mm:ss"
        FormatStyle.MEDIUM -> "H:mm:ss"
        FormatStyle.SHORT -> "H:m:s"
    }

    return LocalTime.Format {
        byUnicodePattern(pattern)
    }
}