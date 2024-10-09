package com.maxkeppeler.sheets.clock

import android.text.format.DateFormat
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
internal actual fun is24HourFormat(): Boolean {
    return DateFormat.is24HourFormat(LocalContext.current)
}