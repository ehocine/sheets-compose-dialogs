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
package com.maxkeppeler.sheets.number.views

import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.maxkeppeker.sheets.core.models.base.LibOrientation
import com.maxkeppeker.sheets.core.utils.TestTags
import com.maxkeppeker.sheets.core.utils.testTags
import com.maxkeppeler.sheets.number.R
import com.maxkeppeler.sheets.number.models.NumberConfig
import com.maxkeppeler.sheets.number.utils.Constants
import com.maxkeppeler.sheets.core.R as RC

/**
 * The item component of the keyboard.
 * @param config The general configuration for the dialog view.
 * @param orientation The orientation of the view.
 * @param key The key that the component represents.
 * @param disabled Whenever the current key is disabled.
 * @param onEnterValue The listener that is invoked when a value was clicked.
 * @param onPrevAction The listener that is invoked when [Constants.ACTION_PREV] was clicked.
 * @param onNextAction The listener that is invoked when [Constants.ACTION_NEXT] was clicked.
 */
@Composable
internal fun KeyItemComponent(
    config: NumberConfig,
    orientation: LibOrientation,
    key: String,
    disabled: Boolean = false,
    onEnterValue: (Int) -> Unit,
    onPrevAction: () -> Unit,
    onNextAction: () -> Unit,
) {
    val haptic = LocalHapticFeedback.current
    val isActionNext = key == Constants.ACTION_NEXT
    val isActionPrev = key == Constants.ACTION_PREV
    var cornerRadius by remember { mutableStateOf(Constants.KEYBOARD_ITEM_CORNER_RADIUS_DEFAULT) }
    val animatedCornerRadius =
        animateIntAsState(cornerRadius, tween(Constants.KEYBOARD_ANIM_CORNER_RADIUS))
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    LaunchedEffect(isPressed, animatedCornerRadius.value) {
        cornerRadius = when {
            isPressed && cornerRadius == Constants.KEYBOARD_ITEM_CORNER_RADIUS_DEFAULT -> Constants.KEYBOARD_ITEM_CORNER_RADIUS_PRESSED
            !isPressed -> Constants.KEYBOARD_ITEM_CORNER_RADIUS_DEFAULT
            else -> cornerRadius
        }
    }

    Row(
        modifier = Modifier
            .testTags(TestTags.KEYBOARD_KEY, key)
            .aspectRatio(1f, true)
            .alpha(if (disabled) Constants.KEYBOARD_ALPHA_ITEM_DISABLED else Constants.KEYBOARD_ALPHA_ITEM_ENABLED)
            .clip(RoundedCornerShape(animatedCornerRadius.value))
            .background(
                if (isActionNext || isActionPrev) MaterialTheme.colorScheme.secondaryContainer
                else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = Constants.KEYBOARD_ACTION_BACKGROUND_SURFACE_ALPHA)
            )
            .clickable(
                enabled = !disabled,
                interactionSource = interactionSource,
                indication = LocalIndication.current,
                onClick = {
                    if (isActionNext) onNextAction()
                    else if (isActionPrev) onPrevAction()
                    else onEnterValue(key.toInt())
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                },
            ),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isActionNext || isActionPrev) {
            val maxSize = dimensionResource(RC.dimen.scd_size_150)
            val minSize = dimensionResource(RC.dimen.scd_size_100)
            Icon(
                modifier = Modifier
                    .padding(dimensionResource(RC.dimen.scd_small_100))
                    .sizeIn(
                        maxWidth = maxSize,
                        maxHeight = maxSize,
                        minWidth = minSize,
                        minHeight = minSize
                    )
                    .fillMaxSize(),
                imageVector = if (isActionNext) config.icons.ChevronRight else config.icons.ChevronLeft,
                contentDescription = stringResource(if (isActionNext) R.string.scd_number_dialog_next_value else R.string.scd_number_dialog_previous_value),
                tint = MaterialTheme.colorScheme.secondary
            )
        } else {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = key,
                    style = with(MaterialTheme.typography) {
                        when (orientation) {
                            LibOrientation.PORTRAIT -> headlineLarge.copy(fontWeight = FontWeight.Bold)
                            LibOrientation.LANDSCAPE -> titleMedium.copy(fontWeight = FontWeight.Bold)
                        }
                    },
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}