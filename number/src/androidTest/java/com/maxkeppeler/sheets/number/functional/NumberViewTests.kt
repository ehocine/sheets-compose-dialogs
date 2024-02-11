/*
 *  Copyright (C) 2022-2023. Maximilian Keppeler (https://www.maxkeppeler.com)
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
@file:OptIn(ExperimentalMaterial3Api::class)

package com.maxkeppeler.sheets.number.functional

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.maxkeppeker.sheets.core.models.base.UseCaseState
import com.maxkeppeker.sheets.core.utils.TestTags
import com.maxkeppeler.sheets.number.NumberView
import com.maxkeppeler.sheets.number.models.NumberConfig
import com.maxkeppeler.sheets.number.models.NumberSelection
import com.maxkeppeler.sheets.number.utils.Constants
import com.maxkeppeler.sheets.test.utils.onNodeWithTags
import com.maxkeppeler.sheets.test.utils.onPositiveButton
import com.maxkeppeler.sheets.test.utils.setContentAndWaitForIdle
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalTime

@RunWith(AndroidJUnit4::class)
class NumberViewTests {

    @get:Rule
    val rule = createComposeRule()

    @Test
    fun clockView_12HourFormat_amSelection_success() {
        val testTime = LocalTime.of(10, 30)
        var selectedTime: LocalTime? = null
        rule.setContentAndWaitForIdle {
            NumberView(
                useCaseState = UseCaseState(visible = true),
                selection = NumberSelection.HoursMinutes { hours, minutes ->
                    selectedTime = LocalTime.of(hours, minutes)
                },
                config = NumberConfig(is24HourFormat = false)
            )
        }
        listOf(1, 0, 3, 0).forEach {
            runBlocking {
                rule.onNodeWithTags(TestTags.KEYBOARD_KEY, it).performClick()
                delay(Constants.DEBOUNCE_KEY_CLICK_DURATION)
            }
        }
        runBlocking {
            rule.onNodeWithTags(TestTags.CLOCK_12_HOUR_FORMAT, 0).performClick()
            delay(Constants.DEBOUNCE_KEY_CLICK_DURATION)
        }
        rule.onPositiveButton().performClick()
        assert(selectedTime == testTime)
    }

    @Test
    fun clockView_12HourFormat_pmSelection_success() {
        val testTime = LocalTime.of(20, 30)
        var selectedTime: LocalTime? = null
        rule.setContentAndWaitForIdle {
            NumberView(
                useCaseState = UseCaseState(visible = true),
                selection = NumberSelection.HoursMinutes { hours, minutes ->
                    selectedTime = LocalTime.of(hours, minutes)
                },
                config = NumberConfig(is24HourFormat = false)
            )
        }
        listOf(0, 8, 3, 0).forEach {
            runBlocking {
                rule.onNodeWithTags(TestTags.KEYBOARD_KEY, it).performClick()
                delay(Constants.DEBOUNCE_KEY_CLICK_DURATION)
            }
        }
        runBlocking {
            rule.onNodeWithTags(TestTags.CLOCK_12_HOUR_FORMAT, 1).performClick()
            delay(Constants.DEBOUNCE_KEY_CLICK_DURATION)
        }
        rule.onPositiveButton().performClick()
        assert(selectedTime == testTime)
    }

    @Test
    fun clockView_12HourFormat_selectionAlwaysValid() {
        rule.setContentAndWaitForIdle {
            NumberView(
                useCaseState = UseCaseState(visible = true),
                selection = NumberSelection.HoursMinutes { _, _ -> },
            )
        }
        rule.onPositiveButton().assertIsEnabled()
    }

    @Test
    fun clockView_12HourFormat_boundaries_within() {
        val testTime = LocalTime.of(10, 30)
        var selectedTime: LocalTime? = null
        rule.setContentAndWaitForIdle {
            NumberView(
                useCaseState = UseCaseState(visible = true),
                selection = NumberSelection.HoursMinutes { hours, minutes ->
                    selectedTime = LocalTime.of(hours, minutes)
                },
                config = NumberConfig(
                    boundary = LocalTime.of(9, 0)..LocalTime.of(11, 0),
                    is24HourFormat = false
                )
            )
        }
        runBlocking {
            rule.onNodeWithTags(TestTags.CLOCK_12_HOUR_FORMAT, 0).performClick()
            delay(Constants.DEBOUNCE_KEY_CLICK_DURATION)
            listOf(1, 0, 3, 0).forEach {
                rule.onNodeWithTags(TestTags.KEYBOARD_KEY, it).performClick()
                delay(Constants.DEBOUNCE_KEY_CLICK_DURATION)
            }
        }
        rule.onPositiveButton().assertIsEnabled()
        rule.onPositiveButton().performClick()
        assert(selectedTime == testTime)
    }

    @Test
    fun clockView_12HourFormat_boundaries_outside() {
        rule.setContentAndWaitForIdle {
            NumberView(
                useCaseState = UseCaseState(visible = true),
                selection = NumberSelection.HoursMinutes { hours, minutes -> },
                config = NumberConfig(
                    boundary = LocalTime.of(1, 0)..LocalTime.of(11, 0),
                    is24HourFormat = false
                )
            )
        }

        runBlocking {
            listOf(1, 2, 0, 0).forEach {
                rule.onNodeWithTags(TestTags.KEYBOARD_KEY, it).performClick()
                delay(Constants.DEBOUNCE_KEY_CLICK_DURATION)
            }
            rule.onNodeWithTags(TestTags.CLOCK_12_HOUR_FORMAT, 0).performClick()
            delay(Constants.DEBOUNCE_KEY_CLICK_DURATION)
        }
        rule.onPositiveButton().assertIsNotEnabled()
    }

    @Test
    fun clockView_12HourFormat_defaultTime_setCorrectly() {
        val defaultTime = LocalTime.of(10, 30)
        var selectedTime: LocalTime? = null
        rule.setContentAndWaitForIdle {
            NumberView(
                useCaseState = UseCaseState(visible = true),
                selection = NumberSelection.HoursMinutes { hours, minutes ->
                    selectedTime = LocalTime.of(hours, minutes)
                },
                config = NumberConfig(
                    defaultTime = defaultTime,
                    is24HourFormat = false
                )
            )
        }
        rule.onPositiveButton().performClick()
        assert(selectedTime == defaultTime)
    }

    @Test
    fun clockView_12HourFormat_defaultTime_notInBoundary_throwsException() {
        val defaultTime = LocalTime.of(10, 30)
        val boundary = LocalTime.of(11, 0)..LocalTime.of(12, 0)
        val result = runCatching {
            rule.setContentAndWaitForIdle {
                NumberView(
                    useCaseState = UseCaseState(visible = true),
                    selection = NumberSelection.HoursMinutes { _, _ -> },
                    config = NumberConfig(
                        defaultTime = defaultTime,
                        boundary = boundary,
                        is24HourFormat = false
                    )
                )
            }
        }
        assert(result.isFailure)
    }

    @Test
    fun clockView_12HourFormat_boundary_inverted_throwsException() {
        val result = runCatching {
            rule.setContentAndWaitForIdle {
                NumberView(
                    useCaseState = UseCaseState(visible = true),
                    selection = NumberSelection.HoursMinutes { _, _ -> },
                    config = NumberConfig(
                        boundary = LocalTime.of(10, 0)..LocalTime.of(5, 0),
                        is24HourFormat = false
                    ),
                )
            }
        }
        assert(result.isFailure)
    }

    @Test
    fun clockView_12HourFormat_boundary_startBoundaryInclusive() {
        rule.setContentAndWaitForIdle {
            NumberView(
                useCaseState = UseCaseState(visible = true),
                selection = NumberSelection.HoursMinutes { hours, minutes ->
                },
                config = NumberConfig(
                    boundary = LocalTime.of(5, 30)..LocalTime.of(6, 0),
                    is24HourFormat = false
                ),
            )
        }
        listOf(5, 3, 0).forEach {
            runBlocking {
                rule.onNodeWithTags(TestTags.KEYBOARD_KEY, it).performClick()
                delay(Constants.DEBOUNCE_KEY_CLICK_DURATION)
            }
            runBlocking {
                rule.onNodeWithTags(TestTags.CLOCK_12_HOUR_FORMAT, 0).performClick()
                delay(Constants.DEBOUNCE_KEY_CLICK_DURATION)
            }
        }
        rule.onPositiveButton().assertIsEnabled()
    }

    @Test
    fun clockView_12HourFormat_boundary_endBoundaryInclusive() {
        rule.setContentAndWaitForIdle {
            NumberView(
                useCaseState = UseCaseState(visible = true),
                selection = NumberSelection.HoursMinutes { hours, minutes ->
                },
                config = NumberConfig(
                    boundary = LocalTime.of(5, 30)..LocalTime.of(6, 0),
                    is24HourFormat = false
                ),
            )
        }
        listOf(6, 0, 0).forEach {
            runBlocking {
                rule.onNodeWithTags(TestTags.KEYBOARD_KEY, it).performClick()
                delay(Constants.DEBOUNCE_KEY_CLICK_DURATION)
            }
        }
        runBlocking {
            rule.onNodeWithTags(TestTags.CLOCK_12_HOUR_FORMAT, 0).performClick()
            delay(Constants.DEBOUNCE_KEY_CLICK_DURATION)
        }
        rule.onPositiveButton().assertIsEnabled()
    }

    @Test
    fun clockView_24HourFormat_selection_success() {
        val testTime = LocalTime.of(10, 30)
        var selectedTime: LocalTime? = null
        rule.setContentAndWaitForIdle {
            NumberView(
                useCaseState = UseCaseState(visible = true),
                selection = NumberSelection.HoursMinutes { hours, minutes ->
                    selectedTime = LocalTime.of(hours, minutes)
                },
                config = NumberConfig(is24HourFormat = true)
            )
        }
        listOf(1, 0, 3, 0).forEach {
            runBlocking {
                rule.onNodeWithTags(TestTags.KEYBOARD_KEY, it).performClick()
                delay(Constants.DEBOUNCE_KEY_CLICK_DURATION)
            }
        }
        rule.onPositiveButton().performClick()
        assert(selectedTime == testTime)
    }

    @Test
    fun clockView_24HourFormat_unsupportedConfigurations_throwsException() {
        val result = runCatching {
            rule.setContentAndWaitForIdle {
                NumberView(
                    useCaseState = UseCaseState(visible = true),
                    selection = NumberSelection.HoursMinutes { _, _ -> },
                    config = NumberConfig(
                        defaultTime = LocalTime.of(4, 0),
                        boundary = LocalTime.of(5, 0)..LocalTime.of(10, 0),
                        is24HourFormat = true,
                    ),
                )
            }
        }
        assert(result.isFailure)
    }

    @Test
    fun clockView_24HourFormat_boundaries_within() {
        val testTime = LocalTime.of(10, 30)
        var selectedTime: LocalTime? = null
        rule.setContentAndWaitForIdle {
            NumberView(
                useCaseState = UseCaseState(visible = true),
                selection = NumberSelection.HoursMinutes { hours, minutes ->
                    selectedTime = LocalTime.of(hours, minutes)
                },
                config = NumberConfig(
                    boundary = LocalTime.of(9, 0)..LocalTime.of(11, 0),
                    is24HourFormat = true
                )
            )
        }
        listOf(1, 0, 3, 0).forEach {
            runBlocking {
                rule.onNodeWithTags(TestTags.KEYBOARD_KEY, it).performClick()
                delay(Constants.DEBOUNCE_KEY_CLICK_DURATION)
            }
        }
        rule.onPositiveButton().assertIsEnabled()
        rule.onPositiveButton().performClick()
        assert(selectedTime == testTime)
    }

    @Test
    fun clockView_24HourFormat_boundary_singleValue() {
        val testTime = LocalTime.of(8, 0)
        var selectedTime: LocalTime? = null
        rule.setContentAndWaitForIdle {
            NumberView(
                useCaseState = UseCaseState(visible = true),
                selection = NumberSelection.HoursMinutes { hours, minutes ->
                    selectedTime = LocalTime.of(hours, minutes)
                },
                config = NumberConfig(
                    is24HourFormat = true,
                    boundary = LocalTime.of(8, 0)..LocalTime.of(8, 0),
                ),
            )
        }
        listOf(8, 0).forEach {
            runBlocking {
                rule.onNodeWithTags(TestTags.KEYBOARD_KEY, it).performClick()
                delay(Constants.DEBOUNCE_KEY_CLICK_DURATION)
            }
        }
        rule.onPositiveButton().performClick()
        assert(selectedTime == testTime)
    }

    @Test
    fun clockView_24HourFormat_boundary_correctlyLimitsSelection() {
        val boundary = LocalTime.of(11, 0)..LocalTime.of(12, 0)
        rule.setContentAndWaitForIdle {
            NumberView(
                useCaseState = UseCaseState(visible = true),
                selection = NumberSelection.HoursMinutes { hours, minutes ->
                },
                config = NumberConfig(boundary = boundary, is24HourFormat = true)
            )
        }
        listOf(1, 0, 0, 0).forEach {
            runBlocking {
                rule.onNodeWithTags(TestTags.KEYBOARD_KEY, it).performClick()
                delay(Constants.DEBOUNCE_KEY_CLICK_DURATION)
            }
        }
        rule.onPositiveButton().assertIsNotEnabled()
    }

    @Test
    fun clockView_12HourFormat_midnightSelection_returnsMidnight() {
        var selectedTime: LocalTime? = null
        rule.setContentAndWaitForIdle {
            NumberView(
                useCaseState = UseCaseState(visible = true),
                selection = NumberSelection.HoursMinutes { hours, minutes ->
                    selectedTime = LocalTime.of(hours, minutes)
                },
                config = NumberConfig(is24HourFormat = false)
            )
        }

        listOf(1, 2, 0, 0).forEach {
            runBlocking {
                rule.onNodeWithTags(TestTags.KEYBOARD_KEY, it).performClick()
                delay(Constants.DEBOUNCE_KEY_CLICK_DURATION)
            }
        }
        runBlocking {
            rule.onNodeWithTags(TestTags.CLOCK_12_HOUR_FORMAT, 0).performClick() // Select AM
            delay(Constants.DEBOUNCE_KEY_CLICK_DURATION)
        }
        rule.onPositiveButton().performClick()

        assert(selectedTime == LocalTime.MIDNIGHT)
    }

    @Test
    fun clockView_12HourFormat_noonSelection_returnsNoon() {
        var selectedTime: LocalTime? = null
        rule.setContentAndWaitForIdle {
            NumberView(
                useCaseState = UseCaseState(visible = true),
                selection = NumberSelection.HoursMinutes { hours, minutes ->
                    selectedTime = LocalTime.of(hours, minutes)
                },
                config = NumberConfig(is24HourFormat = false)
            )
        }

        listOf(1, 2, 0, 0).forEach {
            runBlocking {
                rule.onNodeWithTags(TestTags.KEYBOARD_KEY, it).performClick()
                delay(Constants.DEBOUNCE_KEY_CLICK_DURATION)
            }
        }
        runBlocking {
            rule.onNodeWithTags(TestTags.CLOCK_12_HOUR_FORMAT, 1).performClick() // Select PM
            delay(Constants.DEBOUNCE_KEY_CLICK_DURATION)
        }
        rule.onPositiveButton().performClick()
        assert(selectedTime == LocalTime.NOON)
    }

    @Test
    fun clockView_12HourFormat_amPmIndicatorMisalignment_returnsCorrectTime() {
        var selectedTime: LocalTime? = null
        val testTime = LocalTime.of(10, 30)

        rule.setContentAndWaitForIdle {
            NumberView(
                useCaseState = UseCaseState(visible = true),
                selection = NumberSelection.HoursMinutes { hours, minutes ->
                    selectedTime = LocalTime.of(hours, minutes)
                },
                config = NumberConfig(is24HourFormat = false)
            )
        }
        listOf(1, 0, 3, 0).forEach {
            runBlocking {
                rule.onNodeWithTags(TestTags.KEYBOARD_KEY, it).performClick()
                delay(Constants.DEBOUNCE_KEY_CLICK_DURATION)
            }
        }
        // Change AM/PM without adjusting time again
        runBlocking {
            rule.onNodeWithTags(TestTags.CLOCK_12_HOUR_FORMAT, 1).performClick() // Change to PM
            delay(Constants.DEBOUNCE_KEY_CLICK_DURATION)
        }
        rule.onPositiveButton().performClick()
        assert(selectedTime == testTime.plusHours(12))
    }

    @Test
    fun clockView_12HourFormat_defaultTimeSelection_returnsDefaultTime() {
        val defaultTime = LocalTime.of(10, 30, 0)
        var selectedTime: LocalTime? = null
        rule.setContentAndWaitForIdle {
            NumberView(
                useCaseState = UseCaseState(visible = true),
                selection = NumberSelection.HoursMinutes { hours, minutes ->
                    selectedTime = LocalTime.of(hours, minutes)
                },
                config = NumberConfig(is24HourFormat = false, defaultTime = defaultTime)
            )
        }

        rule.onPositiveButton().performClick()
        assert(selectedTime == defaultTime)
    }

}
