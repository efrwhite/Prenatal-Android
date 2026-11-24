package com.elizabethwhitebaker.prenatal.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import com.elizabethwhitebaker.prenatal.session.Session

@Composable
fun SessionView(
    session: Session,
    modifier: Modifier = Modifier
) {
    val viewModelExtras = MutableCreationExtras().apply {
        set(SessionViewModel.SESSION_KEY, session)
    }
    val viewModel: SessionViewModel = viewModel(
        factory = SessionViewModel.Factory,
        extras = viewModelExtras,
    )
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // The header, which consists of the title, progress bar, and navigation controls
        // The title
        Text(uiState.session.title)
        // The progress bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            // 10dp is an arbitrary figure here; just ensures the buttons don't touch
            // TODO This _has_ to account for display scaling...
            horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally),
            // Necessary or else the progress bar is higher than the labels
            verticalAlignment = Alignment.CenterVertically
        ) {
            val progressPercentage = uiState.page.progress
            Text("Session 1") // TODO This clearly can't stay hardcoded, but sessions aren't numbered
            LinearProgressIndicator(
                progress = { (progressPercentage / 100).toFloat() },
                modifier = Modifier.weight(1f)
            ) // TODO this doesn't animate as-is; should it?
            // Horizontal spacer between progress bar and percentage?
            Text(
                text = "%d%%".format(progressPercentage.toInt()),
                // Forces the text to the rightmost position within its assigned width
                textAlign = TextAlign.Right,
                // Seems to fit 100% starting at 41.dp, at least at the default scaling
                // TODO This _has_ to account for display scaling...
                modifier = Modifier.width(45.dp)
            )
        }
        // The navigation buttons
        Row(
            // Saw recommendations for Modifier.width(IntrinsicSize.Min), but that doesn't seem to help here
            modifier = Modifier.fillMaxWidth(),
            // 10dp is an arbitrary figure here; just ensures the buttons don't touch
            // TODO this _has_ to account for display scaling...
            horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally)
        ) {
            OutlinedButton(
                onClick = { viewModel.previousPage() },
                enabled = uiState.history.isNotEmpty(),
                modifier = Modifier.weight(1f)
            ) {
                Text("Back")
            }

            // Horizontal spacer between buttons?

            OutlinedButton(
                onClick = { viewModel.listen() },
                modifier = Modifier.weight(1f)
            ) {
                // There is no ternary operator in Kotlin, but this does actually work as an expression
                Text(if (uiState.isListening) "Pause" else "Listen")
            }

            // Horizontal spacer between buttons?

            OutlinedButton(
                onClick = { viewModel.reset() },
                modifier = Modifier.weight(1f)
            ) {
                //Text("Exit")
                Text("Reset")
            }
        }

        // Vertical spacer between header and body?
        Spacer(modifier = Modifier.weight(1f))

        // The main page body, rendering any blocks that page has defined
        // I feel like this ideally should not need scrolling, but can we guarantee it?
        uiState.page.blocks.forEach { block ->
            PageBlockView(block)
            // I believe a fixed-width gap is necessary given everything surrounding
            // Arbitrary value copied from an iOS default margin; likely could be changed
            // TODO This _has_ to account for display scaling...
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Vertical spacer between body and footer?
        Spacer(modifier = Modifier.weight(1f))

        ActionButtonView(
            uiState.page.actionButton
        )
    }
}