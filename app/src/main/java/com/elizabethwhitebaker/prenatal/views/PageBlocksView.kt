package com.elizabethwhitebaker.prenatal.views

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.elizabethwhitebaker.prenatal.session.PageBlock
import kotlin.collections.forEach

@Composable
fun PageBlocksView(blocks: List<PageBlock>, modifier: Modifier = Modifier) {
    // TODO remove? Do we need this?
    // Intrinsically scrolls, no need for Modifier.verticalScroll (it'll actually crash if you do)
    LazyColumn(modifier = modifier) {
        blocks.forEach { block ->
            item {
                PageBlockView(block)
            }
        }
    }
}