package com.maxkeppeler.sheets.option.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.maxkeppeker.sheets.core.utils.TestTags
import com.maxkeppeler.sheets.option.models.OptionBody
import com.maxkeppeler.sheets.option.models.OptionConfig

/**
 * The default body component for the rating dialog.
 * @param config The configuration for the rating view.
 * @param body The data of the default body.
 */
@Composable
internal fun DefaultBodyComponent(
    body: OptionBody.Default,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    ) {
        body.preBody()
        Text(
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = body.bodyText,
            style = MaterialTheme.typography.bodyMedium
        )
        body.postBody()
    }
}