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
package com.maxkeppeler.sheets.state.views

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.maxkeppeker.sheets.core.utils.TestTags
import com.maxkeppeler.sheets.state.models.State
import com.maxkeppeler.sheets.state.models.StateConfig

/**
 * The failure state component.
 * @param config The general configuration for the dialog view.
 * @param state The failure state data.
 */
@Composable
internal fun StateFailureComponent(
    config: StateConfig,
    state: State.Failure
) {
    state.customView?.invoke() ?: run {
        Icon(
            modifier = Modifier
                .testTag(TestTags.STATE_FAILURE)
                .padding(vertical = 16.dp)
                .size(56.dp),
            imageVector = config.icons.Error,
            contentDescription = null,
            tint = Color.Red
        )
    }
}