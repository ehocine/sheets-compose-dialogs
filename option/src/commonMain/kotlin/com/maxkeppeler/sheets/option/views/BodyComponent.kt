package com.maxkeppeler.sheets.option.views

import androidx.compose.runtime.Composable
import com.maxkeppeler.sheets.option.models.OptionBody

@Composable
fun OptionBodyComponent(
    body: OptionBody
) {
    when (body) {
        is OptionBody.Custom -> body.body.invoke()
        is OptionBody.Default -> DefaultBodyComponent(body)
    }
}