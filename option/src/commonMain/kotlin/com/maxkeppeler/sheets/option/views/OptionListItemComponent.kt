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
package com.maxkeppeler.sheets.option.views


import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.maxkeppeker.sheets.core.views.IconComponent
import com.maxkeppeler.sheets.option.models.Option
import com.maxkeppeler.sheets.option.models.OptionConfig


/**
 * The list item component for an option.
 * @param config The general configuration.
 * @param option The option that will be displayed.
 * @param modifier The modifier that is applied to this component.
 * @param iconColor The color of the icon.
 * @param textColor The color of the text.
 * @param onInfoClick The listener that is invoked when the info button is clicked.
 */
@Composable
internal fun OptionListItemComponent(
    config: OptionConfig,
    option: Option,
    modifier: Modifier,
    iconColor: Color,
    textColor: Color,
    onInfoClick: () -> Unit,
) {

    Box {
        Column(modifier = modifier) {
            option.listTopView?.invoke(option.selected)
            option.customView?.invoke(option.selected) ?: run {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    option.icon?.let {
                        IconComponent(
                            modifier = Modifier
                                .padding(start = 24.dp)
                                .size(48.dp)
                                .padding(12.dp),
                            iconSource = it,
                            tint = iconColor
                        )
                    }

                    Column(
                        modifier = Modifier
                            .padding(
                                top = 16.dp,
                                bottom = 16.dp,
                                start = 16.dp
                            )
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
            option.listBottomView?.invoke(option.selected)
        }

        InfoContainerComponent(
            config = config,
            modifier = Modifier.align(Alignment.TopEnd),
            optionInfo = option.details,
            onClick = onInfoClick
        )
    }
}
