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
@file:OptIn(ExperimentalFoundationApi::class)

package com.maxkeppeler.sheets.emoji.views

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.maxkeppeker.sheets.core.utils.TestTags
import com.maxkeppeker.sheets.core.utils.testTags
import com.vanniktech.emoji.Emoji


/**
 * The emoji item component.
 * @param emoji The name of the category.
 * @param selectedEmoji The emoji that is currently selected.
 * @param onClick The listener that returns the selected emoji.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun EmojiItemComponent(
    emoji: Emoji,
    selectedEmoji: Emoji?,
    onClick: (Emoji) -> Unit
) {
    val size = remember { mutableStateOf(IntSize.Zero) }
    val showVariants = remember { mutableStateOf(false) }

    VariantsPopup(
        show = showVariants,
        baseEmoji = emoji,
        selectedEmoji = selectedEmoji,
        size = size,
        onClick = onClick,
    )

    BoxWithConstraints(
        modifier = Modifier
            .testTags(TestTags.EMOJI_SELECTION, emoji.unicode)
            .onGloballyPositioned {
                if (size.value != it.size) size.value = it.size
            }
            .clip(MaterialTheme.shapes.small)
            .background(
                when {
                    selectedEmoji == emoji -> MaterialTheme.colorScheme.primaryContainer
                    emoji.variants.any { it == selectedEmoji } -> MaterialTheme.colorScheme.tertiaryContainer
                    else -> Color.Transparent
                }
            )
            .combinedClickable(
                onClick = { onClick(emoji) },
                onLongClick = {
                    if (emoji.variants.isNotEmpty()) showVariants.value = true
                }
            )
            .padding(2.dp),
    ) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            fontSize = constraints.maxWidth.sp,
            text = emoji.unicode
        )
    }
}