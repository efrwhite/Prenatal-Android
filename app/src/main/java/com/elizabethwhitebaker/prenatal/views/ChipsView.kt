package com.elizabethwhitebaker.prenatal.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.material3.FilledTonalToggleButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.elizabethwhitebaker.prenatal.ChipsState
import com.elizabethwhitebaker.prenatal.session.PageBlock

@Composable
fun ChipsView(
    chipsBlock: PageBlock.ChipsBlock,
    viewModel: SessionViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var blockState: ChipsState? = uiState.blockStates[chipsBlock.savedName] as? ChipsState

    if (blockState == null) {
        // By the power of smart casting, since this is now guaranteed to have a value, it will unwrap automatically
        blockState = ChipsState(chipsBlock.choices)
    }

    FlowRow(
        // 10dp is an arbitrary figure here; just ensures the buttons don't touch
        // TODO this _has_ to account for display scaling...
        horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally),
        verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically)
    ) {
        for ((choiceName, choiceSelected) in blockState.choicesMap) {
            FilledTonalToggleButton(
                checked = choiceSelected,
                onCheckedChange = {
                    val newChoices = if (choiceSelected || chipsBlock.multiSelect) {
                        // If the choice is being deselected, or multi-select is enabled, just toggle the value
                        blockState.choicesMap.plus(Pair(choiceName, !choiceSelected))
                    } else {
                        // If the choice is being selected and multi-select is disabled, clear all selections then set this choice true
                        blockState.choicesMap.mapValues { false }.plus(Pair(choiceName, true))
                    }
                    viewModel.setBlockState(chipsBlock.savedName, ChipsState(newChoices))
                }
            ) {
                Text(choiceName)
            }
        }
    }
}