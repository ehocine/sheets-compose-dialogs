package com.maxkeppeler.sheets.option.models

import androidx.compose.runtime.Composable

/**
 * Defined implementations of a body for the rating dialog.
 */
abstract class OptionBody {

    /**
     * Standard implementation of a body.
     * @param bodyText Text that will set as the title
     * @param preBody Content that is added before the body text.
     * @param postBody Content that is added after the body text.
     */
    data class Default(
        val bodyText: String,
        val preBody: @Composable () -> Unit = {},
        val postBody: @Composable () -> Unit = {}
    ) : OptionBody()

    /**
     * Custom implementation of a body.
     */
    data class Custom(
        val body: @Composable () -> Unit
    ) : OptionBody()
}