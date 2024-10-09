package com.maxkeppeler.sheets.date_time.utils

import kotlinx.datetime.*

internal fun LocalDate.Companion.now(): LocalDate {
    return Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
}

/**
 * Extension function that gets the maximum days of month
 *
 * @return [Int] of days in month
 */
internal fun Month.length(leapYear: Boolean): Int = when (this) {
    Month.JANUARY -> 31
    Month.FEBRUARY -> {
        if (leapYear) {
            29
        } else {
            28
        }
    }
    Month.MARCH -> 31
    Month.APRIL -> 30
    Month.MAY -> 31
    Month.JUNE -> 30
    Month.JULY -> 31
    Month.AUGUST -> 31
    Month.SEPTEMBER -> 30
    Month.OCTOBER -> 31
    Month.NOVEMBER -> 30
    Month.DECEMBER -> 31
    else -> 30
}

/**
 * Extension function that gets the maximum days of month
 *
 * @return [Int] of days in month
 */
internal val LocalDate.lengthOfMonth: Int
    get() = month.length(isLeapYear)

/**
 * Extension function that checks if year is a leap year.
 *
 * Example: 2016, 2020, 2024, etc
 *
 * @return [Boolean] whether year is a leap year
 */
internal val LocalDate.isLeapYear: Boolean
    get() {
        var isLeapYear = year % 4 == 0
        isLeapYear = isLeapYear && (year % 100 != 0 || year % 400 == 0)

        return isLeapYear
    }