package com.elizabethwhitebaker.prenatal.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.elizabethwhitebaker.prenatal.session.actionButton.ActionButton
import com.elizabethwhitebaker.prenatal.session.actionButton.ActionButtonType

@Composable
fun ActionButtonView(
    // TODO maybe this can be ascertained from the ViewModel instead of passed?
    actionButton: ActionButton,
    viewModel: SessionViewModel = viewModel()
) {

    Column(
        // TODO Would space the button and any subscript, but is that necessary?
        //verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        // TODO These cases can probably be consolidated, if nothing else into the 1/2 button styles
        when (actionButton.type) {
            ActionButtonType.NEXT -> {
                Button(
                    onClick = { viewModel.nextPage() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(actionButton.text)
                }
            }

            ActionButtonType.SKIP_OR_NEXT -> {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    // 10dp is an arbitrary figure here; just ensures the buttons don't touch
                    // TODO this _has_ to account for display scaling...
                    horizontalArrangement = Arrangement.spacedBy(
                        10.dp,
                        Alignment.CenterHorizontally
                    )
                ) {
                    Button(
                        onClick = { viewModel.skip() },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Skip")
                    }

                    Button(
                        onClick = { viewModel.nextPage() },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(actionButton.text)
                    }
                }
            }

            ActionButtonType.CUSTOM_OR_NEXT -> {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    // 10dp is an arbitrary figure here; just ensures the buttons don't touch
                    // TODO this _has_ to account for display scaling...
                    horizontalArrangement = Arrangement.spacedBy(
                        10.dp,
                        Alignment.CenterHorizontally
                    )
                ) {
                    Button(
                        onClick = { viewModel.goToPage(actionButton.customDestination!!) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(actionButton.customText!!)
                    }

                    Button(
                        onClick = { viewModel.nextPage() },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(actionButton.text)
                    }
                }
            }

            ActionButtonType.CUSTOM -> {
                Button(
                    onClick = { viewModel.goToPage(actionButton.destination!!) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(actionButton.text)
                }
            }

            ActionButtonType.FINISH -> {
                Button(
                    onClick = { viewModel.reset() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(/*actionButton.text*/"Reset") // TODO change the text ofc
                }
            }
        }

        actionButton.subscript?.let {
            Text(
                it,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}