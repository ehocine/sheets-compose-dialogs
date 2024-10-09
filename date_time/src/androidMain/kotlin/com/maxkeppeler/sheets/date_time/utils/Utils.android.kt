package com.maxkeppeler.sheets.date_time.utils

import java.time.chrono.Chronology
import java.time.format.DateTimeFormatterBuilder
import java.util.Locale

internal fun FormatStyle.toJava(): java.time.format.FormatStyle {
    return when (this) {
        FormatStyle.FULL -> java.time.format.FormatStyle.FULL
        FormatStyle.LONG -> java.time.format.FormatStyle.LONG
        FormatStyle.MEDIUM -> java.time.format.FormatStyle.MEDIUM
        FormatStyle.SHORT -> java.time.format.FormatStyle.SHORT
    }
}

internal actual fun getLocalizedPattern(
    isDate: Boolean,
    formatStyle: FormatStyle
): String {
    val locale = Locale.getDefault()
    return DateTimeFormatterBuilder.getLocalizedDateTimePattern(
        if (isDate) formatStyle.toJava() else null,
        if (!isDate) formatStyle.toJava() else null, Chronology.ofLocale(locale), locale
    ).toString()
}