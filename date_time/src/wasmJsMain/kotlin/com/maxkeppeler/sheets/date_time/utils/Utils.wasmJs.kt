package com.maxkeppeler.sheets.date_time.utils

internal actual fun getLocalizedPattern(
    isDate: Boolean,
    formatStyle: FormatStyle
): String {
    return if (isDate) {
        when (formatStyle) {
            FormatStyle.FULL -> "EEEE, MMMM d, yyyy G"
            FormatStyle.LONG -> "MMMM d, yyyy"
            FormatStyle.MEDIUM -> "MMM d, yyyy"
            FormatStyle.SHORT -> "dd.MM.yyyy"
        }
    } else {
        when (formatStyle) {
            FormatStyle.FULL -> "HH:mm:ss zzz"
            FormatStyle.LONG -> "HH:mm:ss"
            FormatStyle.MEDIUM -> "H:mm:ss"
            FormatStyle.SHORT -> "H:m:s"
        }
    }
}