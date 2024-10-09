/*
 *  Copyright (C) 2022-2024. Maximilian Keppeler (https://www.maxkeppeler.com)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.maxkeppeler.sheets.date_time.utils

import com.maxkeppeler.sheets.date_time.models.DateTimeConfig
import com.maxkeppeler.sheets.date_time.models.UnitOptionEntry
import com.maxkeppeler.sheets.date_time.models.UnitSelection
import com.maxkeppeler.sheets.date_time.models.UnitType
import kotlinx.datetime.*
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern
import org.jetbrains.compose.resources.ExperimentalResourceApi
import sheets_compose_dialogs.date_time.generated.resources.Res
import sheets_compose_dialogs.date_time.generated.resources.sheets_compose_dialogs_date_time_am
import sheets_compose_dialogs.date_time.generated.resources.sheets_compose_dialogs_date_time_pm

internal fun getMapOptions(
    datePattern: String?,
    timePattern: String?
): Map<UnitType, List<UnitOptionEntry>> = mapOf(
    UnitType.SECOND to getMinutesSecondsOptions(),
    UnitType.MINUTE to getMinutesSecondsOptions(),
    UnitType.HOUR to getHoursOptions(timePattern ?: ""),
    UnitType.DAY to getDayOptions(LocalDate.now(), null),
    UnitType.MONTH to getMonthOptions(datePattern ?: ""),
    UnitType.YEAR to getYearOptions(DateTimeConfig()),
    UnitType.AM_PM to getAmPmOptions()
)

internal fun detectUnit(
    config: DateTimeConfig,
    pattern: String,
    segment: String,
    unitValues: MutableMap<UnitType, UnitOptionEntry?>,
): UnitSelection? {
    val now = LocalDate.now()
    val month = unitValues[UnitType.MONTH]
    val date = LocalDate(
        year = unitValues[UnitType.YEAR]?.value ?: now.year,
        monthNumber = month?.value ?: now.monthNumber,
        dayOfMonth = 1
    )
    return when {
        segment.contains(Constants.SYMBOL_SECONDS) ->
            UnitSelection.Second(
                value = unitValues[UnitType.SECOND],
                options = getMinutesSecondsOptions()
            )
        segment.contains(Constants.SYMBOL_MINUTES) ->
            UnitSelection.Minute(
                value = unitValues[UnitType.MINUTE],
                options = getMinutesSecondsOptions()
            )
        segment.contains(Constants.SYMBOL_HOUR, ignoreCase = true) ->
            UnitSelection.Hour(
                value = unitValues[UnitType.HOUR],
                options = getHoursOptions(pattern)
            )
        segment.contains(Constants.SYMBOL_DAY) ->
            UnitSelection.Day(
                value = unitValues[UnitType.DAY],
                options = getDayOptions(date, month)
            )
        segment.contains(Constants.SYMBOL_MONTH) ->
            UnitSelection.Month(
                value = unitValues[UnitType.MONTH],
                options = getMonthOptions(segment)
            )
        segment.contains(Constants.SYMBOL_24_HOUR_FORMAT) -> {
            UnitSelection.AmPm(
                value = unitValues[UnitType.AM_PM],
                options = getAmPmOptions()
            )
        }
        segment.contains(Constants.SYMBOL_YEAR, ignoreCase = true) ->
            UnitSelection.Year(
                value = unitValues[UnitType.YEAR],
                options = getYearOptions(config)
            )

        else -> null
    }
}

internal fun getInitTypeValues(
    dateSelection: LocalDate?,
    timeSelection: LocalTime?,
    datePattern: String?,
    timePattern: String?
): MutableMap<UnitType, UnitOptionEntry?> {
    val options = getMapOptions(datePattern, timePattern)
    val second = timeSelection?.let { options[UnitType.SECOND]?.getOrNull(it.second) }
    val minute = timeSelection?.let { options[UnitType.MINUTE]?.getOrNull(it.minute) }

    val is24HourFormat = is24HourFormat(timePattern)

    val amPm = timeSelection?.let {
        val item = if (is24HourFormat) 0 else 1
        options[UnitType.AM_PM]?.getOrNull(item)
    }

    val hour = timeSelection?.let {
        // 0-23 = 24h format, 1-12 = 12h format (minus 1 because of 0 index)
        val hour = if (is24HourFormat) it.hour else (it.hour % 12) - 1
        options[UnitType.HOUR]?.getOrNull(hour)
    }

    val year = dateSelection?.let { date -> options[UnitType.YEAR]?.let { it.firstOrNull { it.value == date.year } } }
    val month = dateSelection?.let { options[UnitType.MONTH]?.getOrNull(it.monthNumber.minus(1)) }
    val day = dateSelection?.let { options[UnitType.DAY]?.getOrNull(it.dayOfMonth.minus(1)) }

    return mutableMapOf(
        // Date
        UnitType.DAY to day,
        UnitType.MONTH to month,
        UnitType.YEAR to year,
        // Time
        UnitType.SECOND to second,
        UnitType.MINUTE to minute,
        UnitType.HOUR to hour,
        UnitType.AM_PM to amPm
    )
}

internal fun getLocalTimeOf(
    isAm: Boolean?,
    values: List<UnitOptionEntry?>,
) = runCatching {

    val hourValue = values[2]!!.value
    val minValue = values[1]!!.value
    val secValue = values[0]?.value ?: 0
    var actualHourValue = hourValue

    isAm?.let {
        if (isAm && actualHourValue >= 12 && minValue > 0) actualHourValue -= 12
        else if (!isAm && ((actualHourValue < 12 && minValue >= 0) || (actualHourValue == 12 && minValue == 0))) actualHourValue += 12
        if (actualHourValue == 24) actualHourValue = 0
    }

    LocalTime(
        hour = actualHourValue,
        minute = minValue,
        second = secValue
    )

}.getOrNull()

internal fun getLocalDateOf(
    values: List<UnitOptionEntry?>,
) = runCatching {
    LocalDate(
        values[2]!!.value,
        values[1]!!.value,
        values[0]!!.value
    )
}.getOrNull()

internal expect fun getLocalizedPattern(
    isDate: Boolean,
    formatStyle: FormatStyle
): String

internal fun getLocalizedValues(
    config: DateTimeConfig,
    pattern: String?,
    unitValues: MutableMap<UnitType, UnitOptionEntry?>
): List<List<Any?>>? {
    val values = pattern?.split(" ", ".", ":", "-")?.toTypedArray()
    return values?.map { value ->
        val segments = getLocalizedValueSegments(value)
        segments.map { segment ->
            if (!config.hideDateCharacters && segment.isEmpty()) segment
            else detectUnit(
                config = config,
                pattern = pattern,
                segment = segment,
                unitValues = unitValues
            )
        }
    }
}

internal fun getLocalizedValueSegments(segment: String): List<String> =
    segment.split(",", ".").dropLastWhile { it.isEmpty() }

internal fun is24HourFormat(pattern: String?): Boolean =
    !containsAmPm(pattern)

private fun containsAmPm(pattern: String?): Boolean =
    pattern?.contains(Constants.SYMBOL_24_HOUR_FORMAT) == true

internal fun containsSeconds(pattern: String): Boolean = pattern.contains(Constants.SYMBOL_SECONDS)

@OptIn(ExperimentalResourceApi::class)
internal fun getAmPmOptions() = listOf(
    UnitOptionEntry(value = 0, labelRes = Res.string.sheets_compose_dialogs_date_time_am),
    UnitOptionEntry(value = 1, labelRes = Res.string.sheets_compose_dialogs_date_time_pm),
)

internal fun getMinutesSecondsOptions(): List<UnitOptionEntry> {
    return (0..59).map {
        UnitOptionEntry(
            it,
            it.toString().padStart(2, '0')
        )
    }.toList()
}

internal fun getHoursOptions(pattern: String): List<UnitOptionEntry> =
    if (is24HourFormat(pattern)) {
        (0..23).map { value ->
            UnitOptionEntry(
                value = value,
                label = value.toString().padStart(2, '0')
            )
        }.toList()
    } else {
        (1..12).map { value ->
            UnitOptionEntry(
                value = value,
                label = value.toString()
            )
        }.toList()
    }

internal fun getDayOptions(date: LocalDate, month: UnitOptionEntry?): List<UnitOptionEntry> {
    val daysInMonth = date.lengthOfMonth
    return (1..(if (month?.value != null) 31 else daysInMonth)).map {
        UnitOptionEntry(
            it,
            it.toString()
        )
    }
}

@OptIn(FormatStringsInDatetimeFormats::class)
internal fun getMonthOptions(pattern: String): List<UnitOptionEntry> {
    val actualMonthPattern = pattern.filter { it == 'M' }
    val occurrences = actualMonthPattern.count { it == 'M' }
    return when {
        occurrences >= 3 -> {
            Month.entries.map { month ->
                val currentDate = LocalDate.now()
                val localDate = LocalDate(
                    year = currentDate.year,
                    monthNumber = month.number,
                    dayOfMonth = currentDate.dayOfMonth
                )
                UnitOptionEntry(
                    month.number,
                    localDate.format(LocalDate.Format {
                        byUnicodePattern(actualMonthPattern)
                    })
                )
            }
        }
        else -> Month.entries.map { it.number }.map {
            UnitOptionEntry(it, it.toString())
        }.toList()
    }
}

internal fun getYearOptions(config: DateTimeConfig): List<UnitOptionEntry> =
    IntRange(config.minYear, config.maxYear.plus(1)).map { value ->
        UnitOptionEntry(
            value = value,
            label = value.toString()
        )
    }.reversed()