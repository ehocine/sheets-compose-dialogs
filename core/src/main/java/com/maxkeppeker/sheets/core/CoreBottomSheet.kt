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

package com.maxkeppeker.sheets.core

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import com.maxkeppeker.sheets.core.models.CoreSelection
import com.maxkeppeker.sheets.core.models.base.Header
import com.maxkeppeker.sheets.core.models.base.UseCaseState
import com.maxkeppeker.sheets.core.views.base.BottomSheetBase

/**
 * Core popup that functions as the base of a custom use-case.
 * @param state The state of the sheet.
 * @param selection The selection configuration for the dialog.
 * @param header The header to be displayed at the top of the dialog.
 * @param body The body content to be displayed inside the dialog.
 * @param onPositiveValid If the positive button is valid and therefore enabled.
 * @param allowDismiss If the sheet can be dismissed by swiping down.
 */
@ExperimentalMaterial3Api
@Composable
fun CoreBottomSheet(
    state: UseCaseState,
    selection: CoreSelection,
    header: Header? = null,
    body: @Composable () -> Unit,
    onPositiveValid: Boolean = true,
    allowDismiss: Boolean = true,
) {

    BottomSheetBase(
        state = state,
        allowDismiss = allowDismiss,
    ) {
        CoreView(
            useCaseState = state,
            selection = selection,
            header = header,
            body = body,
            onPositiveValid = onPositiveValid
        )
    }
}