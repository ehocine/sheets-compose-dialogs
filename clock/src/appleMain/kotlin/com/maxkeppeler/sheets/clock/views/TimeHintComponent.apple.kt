package com.maxkeppeler.sheets.clock.views

import com.maxkeppeler.sheets.clock.utils.FormatStyle
import kotlinx.datetime.LocalTime
import kotlinx.datetime.format.DateTimeFormat
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern
import platform.Foundation.*

internal fun FormatStyle.toSwift(): NSDateFormatterStyle {
    return when (this) {
        FormatStyle.FULL -> NSDateFormatterFullStyle
        FormatStyle.LONG -> NSDateFormatterLongStyle
        FormatStyle.MEDIUM -> NSDateFormatterMediumStyle
        FormatStyle.SHORT -> NSDateFormatterShortStyle
    }
}

@OptIn(FormatStringsInDatetimeFormats::class)
internal actual fun getTimeFormatter(style: FormatStyle): DateTimeFormat<LocalTime> {
    val formatter = NSDateFormatter()
    formatter.setDateStyle(NSDateFormatterNoStyle)
    formatter.setTimeStyle(style.toSwift())
    val pattern = formatter.dateFormat()

    return LocalTime.Format {
        byUnicodePattern(pattern)
    }
}