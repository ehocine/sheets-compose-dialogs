package com.maxkeppeler.sheets.clock

import androidx.compose.runtime.Composable
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSLocale
import platform.Foundation.currentLocale

@Composable
internal actual fun is24HourFormat(): Boolean {
    val dateFormat = NSDateFormatter.dateFormatFromTemplate("j", 0u, NSLocale.currentLocale)
    return dateFormat?.contains('a')?.not() ?: true
}