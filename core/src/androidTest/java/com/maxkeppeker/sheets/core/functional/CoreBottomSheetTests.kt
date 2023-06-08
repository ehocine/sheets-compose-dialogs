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

package com.maxkeppeker.sheets.core.functional

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.maxkeppeker.sheets.core.CoreBottomSheet
import com.maxkeppeker.sheets.core.models.CoreSelection
import com.maxkeppeker.sheets.core.models.base.SelectionButton
import com.maxkeppeker.sheets.core.models.base.UseCaseState
import com.maxkeppeler.sheets.test.utils.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalMaterial3Api::class)
@RunWith(AndroidJUnit4::class)
class CoreBottomSheetTests {

    @get:Rule
    val rule = createComposeRule()
    @Test
    fun coreBottomSheet_visible() {
        rule.setContentAndWaitForIdle {
            CoreBottomSheet(
                state = UseCaseState(visible = true),
                selection = CoreSelection(),
                body = { },
            )
        }
        rule.onBottomSheet().assertExists()
        rule.onBottomSheet().assertIsDisplayed()
    }

    @Test
    fun coreBottomSheet_notVisible() {
        rule.setContentAndWaitForIdle {
            CoreBottomSheet(
                state = UseCaseState(visible = false),
                selection = CoreSelection(),
                body = { },
            )
        }
        rule.onBottomSheet().assertDoesNotExist()
    }

    @Test
    fun coreBottomSheet_invokesPositiveButton() {
        var positiveCalled = false
        rule.setContent {
            CoreBottomSheet(
                state = UseCaseState(visible = true),
                selection = CoreSelection(
                    onPositiveClick = { positiveCalled = true },
                ),
                body = { },
            )
        }
        rule.onPositiveButton().performClick()
        rule.onBottomSheet().assertDoesNotExist()
        assert(positiveCalled)
    }

    @Test
    fun coreBottomSheet_invokesNegativeButton() {
        var negativeCalled = false
        rule.setContent {
            CoreBottomSheet(
                state = UseCaseState(visible = true),
                selection = CoreSelection(
                    onNegativeClick = { negativeCalled = true },
                ),
                body = { },
            )
        }
        rule.onNegativeButton().performClick()
        rule.onBottomSheet().assertDoesNotExist()
        assert(negativeCalled)
    }

    @Test
    fun coreBottomSheet_invokesExtraButton() {
        var extraCalled = false
        rule.setContent {
            CoreBottomSheet(
                state = UseCaseState(visible = true),
                selection = CoreSelection(
                    extraButton = SelectionButton("test"),
                    onExtraButtonClick = { extraCalled = true },
                ),
                body = { },
            )
        }
        rule.onExtraButton().performClick()
        rule.onBottomSheet().assertExists()
        assert(extraCalled)
    }

    @Test
    fun coreBottomSheet_displaysBody() {
        var bodyCalled = false
        rule.setContent {
            CoreBottomSheet(
                selection = CoreSelection(),
                state = UseCaseState(visible = true),
                body = { bodyCalled = true },
            )
        }
        assert(bodyCalled)
    }
}