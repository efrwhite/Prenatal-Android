package com.elizabethwhitebaker.prenatal.views

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.elizabethwhitebaker.prenatal.session.PageBlock

@Composable
fun PageBlockView(block: PageBlock) {
    when (block) {
        is PageBlock.TextBlock -> {
            MarkdownTextComponent(block.data)
        }

        is PageBlock.TitleBlock -> {
            MarkdownTextComponent(
                markdown = block.data,
                style = MaterialTheme.typography.headlineLarge
            )
        }

        is PageBlock.ImageBlock -> {
            // TODO Perhaps change this to an ID lookup map? This is quite *heavily* discouraged.
            val context = LocalContext.current
            val drawableId = remember(block.name) {
                @SuppressLint("LocalContextResourcesRead") context.resources.getIdentifier(
                    block.name,
                    "drawable",
                    context.packageName
                )
            }
            if (drawableId == 0) {
                Text("Failed to load image (${block.name})", fontWeight = FontWeight.Bold)
            } else {
                // TODO Do we want readable descriptors of drawn images?
                Image(painter = painterResource(drawableId), contentDescription = null)
            }
        }

        is PageBlock.ListBlock -> {
            ListView(block)
        }

        is PageBlock.HorizontalRuleBlock -> {
            HorizontalDivider()
        }

        is PageBlock.ChipsBlock -> {
            ChipsView(block)
        }

        // TODO remove this later, probably
        else -> {
            Text("TODO Implement! Type ${block::class.simpleName}")
        }

    }
}