package com.maxkeppeler.sheets.date_time.utils

import platform.Foundation.*

internal fun FormatStyle.toSwift(isAbsent: Boolean): NSDateFormatterStyle {
    if (isAbsent) {
        return NSDateFormatterNoStyle
    }
    return when (this) {
        FormatStyle.FULL -> NSDateFormatterFullStyle
        FormatStyle.LONG -> NSDateFormatterLongStyle
        FormatStyle.MEDIUM -> NSDateFormatterMediumStyle
        FormatStyle.SHORT -> NSDateFormatterShortStyle
    }
}

internal actual fun getLocalizedPattern(
    isDate: Boolean,
    formatStyle: FormatStyle
): String {
    val formatter = NSDateFormatter()
    formatter.setDateStyle(formatStyle.toSwift(!isDate))
    formatter.setTimeStyle(formatStyle.toSwift(isDate))
    return formatter.dateFormat()
}