package com.maxkeppeler.sheets.clock.views

import com.maxkeppeler.sheets.clock.utils.FormatStyle
import kotlinx.datetime.LocalTime
import kotlinx.datetime.format.DateTimeFormat
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern
import java.time.chrono.Chronology
import java.time.format.DateTimeFormatterBuilder
import java.util.*

internal fun FormatStyle.toJava(): java.time.format.FormatStyle {
    return when (this) {
        FormatStyle.FULL -> java.time.format.FormatStyle.FULL
        FormatStyle.LONG -> java.time.format.FormatStyle.LONG
        FormatStyle.MEDIUM -> java.time.format.FormatStyle.MEDIUM
        FormatStyle.SHORT -> java.time.format.FormatStyle.SHORT
    }
}

@OptIn(FormatStringsInDatetimeFormats::class)
internal actual fun getTimeFormatter(style: FormatStyle): DateTimeFormat<LocalTime> {
    val locale = Locale.getDefault()
    val pattern = DateTimeFormatterBuilder.getLocalizedDateTimePattern(
        null, style.toJava(), Chronology.ofLocale(locale), locale
    ).toString()

    return LocalTime.Format {
        byUnicodePattern(pattern)
    }
}