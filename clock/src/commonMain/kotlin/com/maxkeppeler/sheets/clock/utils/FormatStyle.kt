package com.maxkeppeler.sheets.clock.utils

/**
 * Enumeration of the style of a localized date, time or date-time formatter.
 *
 * These styles are used when obtaining a date-time style from configuration.
 */
enum class FormatStyle {
    // ordered from large to small
    /**
     * Full text style, with the most detail.
     * For example, the format might be 'Tuesday, April 12, 1952 AD' or '3:30:42pm PST'.
     */
    FULL,

    /**
     * Long text style, with lots of detail.
     * For example, the format might be 'January 12, 1952'.
     */
    LONG,

    /**
     * Medium text style, with some detail.
     * For example, the format might be 'Jan 12, 1952'.
     */
    MEDIUM,

    /**
     * Short text style, typically numeric.
     * For example, the format might be '12.13.52' or '3:30pm'.
     */
    SHORT
}
