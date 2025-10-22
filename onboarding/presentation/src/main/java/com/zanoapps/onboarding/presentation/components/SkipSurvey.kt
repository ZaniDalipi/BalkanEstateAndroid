package com.zanoapps.onboarding.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun SkipSurvey(
    onSkip: () -> Unit,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {

        TextButton(
            onClick = onSkip,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(
                text = "Skip survey and explore properties",
                color = MaterialTheme.colorScheme.primary
            )
        }
    }

}
