package com.elizabethwhitebaker.prenatal.views

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.elizabethwhitebaker.prenatal.session.ListType
import com.elizabethwhitebaker.prenatal.session.PageBlock

@Composable
fun ListView(listBlock: PageBlock.ListBlock) {
    ListView(listBlock, 1)
}

@Composable
private fun ListView(listBlock: PageBlock.ListBlock, level: Int) {
    // Arbitrary value copied from an iOS default margin; likely could be changed,
    // but would need to match SessionView
    // TODO this _has_ to account for display scaling...
    val bulletSize = if (listBlock.type == ListType.NONE) {
        // Ensure we don't have any padding for non-indicated lists
        0.dp
    } else {
        16.dp
    }
    // Monospaced bullets (literal) look nicer
    val bulletFont = if (listBlock.type == ListType.BULLET) {
        FontFamily.Monospace
    } else {
        null
    }
    // Indent one margin-width per level
    // Bullet itself precedes the block, thus level - 1,
    val elementPadding = bulletSize * (level - 1)

    for ((elementNumber, element) in listBlock.elements.withIndex()) {
        if (element is PageBlock.ListBlock) {
            // If the element is itself a list, it handles itself, but needs to know its level
            ListView(element, level + 1)
        } else {
            // Row to keep the bullet and element horizontally adjacent
            // Alignment keeps bullet height aligned with text height instead of offset
            // We're already handling horizontal spacing directly
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = elementPadding)
            ) {
                Text(
                    // The bullet's textual representation (incl. nothing)
                    // The withIndex() indices start at 0, which makes no sense for a visual list
                    getBulletText(listBlock, elementNumber + 1),
                    // The pre-calculated monospace status
                    fontFamily = bulletFont,
                    // Aligned to the left of its assigned space
                    textAlign = TextAlign.Left,
                    // TODO This may fail on longer bullet Strings; does that matter?
                    // Assigning its space so the element is appropriately to the side
                    modifier = Modifier.width(bulletSize)
                )
                // The actual block we're here for!
                PageBlockView(element)
            }
        }
    }
}

// External when statement that returns a String for each bullet type
private fun getBulletText(listBlock: PageBlock.ListBlock, elementNumber: Int): String {
    return when (listBlock.type) {
        ListType.BULLET -> "•"
        // Formats the string akin to 1.
        ListType.DECIMAL -> "$elementNumber."
        ListType.NONE -> ""
    }
}