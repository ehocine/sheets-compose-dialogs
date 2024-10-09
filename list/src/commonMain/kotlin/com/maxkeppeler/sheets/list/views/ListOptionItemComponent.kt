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
package com.maxkeppeler.sheets.list.views


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.maxkeppeker.sheets.core.utils.TestTags
import com.maxkeppeker.sheets.core.utils.testTags
import com.maxkeppeker.sheets.core.views.IconComponent
import com.maxkeppeler.sheets.list.models.ListOption
import com.maxkeppeler.sheets.list.models.ListSelection

/**
 * The item component for an option.
 * @param selection The selection configuration.
 * @param option The option that will be displayed.
 * @param inputDisabled If input is disabled.
 * @param onClick Listener that is invoked when the option is clicked.
 */
@Composable
internal fun ListOptionItemComponent(
    selection: ListSelection,
    option: ListOption,
    inputDisabled: Boolean,
    onClick: (ListOption) -> Unit
) {

    val showCheckBox = selection is ListSelection.Multiple && selection.showCheckBoxes
    val showRadioButtons = selection is ListSelection.Single && selection.showRadioButtons
    val showSelectionView = showCheckBox || showRadioButtons

    val backgroundColor =
        if (!showSelectionView && option.selected) MaterialTheme.colorScheme.secondaryContainer
        else MaterialTheme.colorScheme.surfaceVariant

    val iconColor = if (option.selected) option.icon?.selectedTint ?: option.icon?.tint
    ?: MaterialTheme.colorScheme.primary
    else option.icon?.tint ?: MaterialTheme.colorScheme.onSurface

    val textColor = if (!showSelectionView && option.selected) MaterialTheme.colorScheme.primary
    else MaterialTheme.colorScheme.onSurface

    val containerModifier = Modifier
        .testTags(TestTags.LIST_VIEW_SELECTION, option.position)
        .fillMaxWidth()
        .clip(MaterialTheme.shapes.medium)
        .clickable(!inputDisabled) { onClick(option) }
        .then(if (!showSelectionView && option.selected) Modifier.background(backgroundColor) else Modifier)

    Row(
        modifier = containerModifier,
        verticalAlignment = Alignment.CenterVertically
    ) {

        val selectionModifier = Modifier
        when {
            showCheckBox -> Checkbox(
                modifier = selectionModifier,
                checked = option.selected,
                onCheckedChange = { onClick(option) }
            )
            showRadioButtons -> RadioButton(
                modifier = selectionModifier,
                selected = option.selected,
                onClick = { onClick(option) }
            )
        }

        option.icon?.let {
            IconComponent(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .size(48.dp),
                iconSource = it,
                tint = iconColor
            )
        }

        Column(
            modifier = Modifier
                .padding(vertical = 12.dp)
                .padding(start = 16.dp)
                .fillMaxWidth()
                .wrapContentHeight(),
        ) {

            Text(
                maxLines = 1,
                text = option.titleText,
                style = MaterialTheme.typography.labelLarge.copy(color = textColor)
            )

            option.subtitleText?.let { text ->
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodySmall.copy(color = textColor)
                )
            }
        }
    }
}
