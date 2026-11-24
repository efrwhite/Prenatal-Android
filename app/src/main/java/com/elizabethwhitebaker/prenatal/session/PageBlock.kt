package com.elizabethwhitebaker.prenatal.session

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonClassDiscriminator(SessionSerializer.BLOCK_DISCRIMINATOR)
sealed class PageBlock {
    @Serializable
    @SerialName("text")
    data class TextBlock(val data: String) : PageBlock()

    @Serializable
    @SerialName("title")
    data class TitleBlock(val data: String) : PageBlock()

    @Serializable
    @SerialName("image")
    data class ImageBlock(val name: String) : PageBlock()

    @Serializable
    @SerialName("list")
    data class ListBlock(val type: ListType, val elements: List<PageBlock>) : PageBlock()

    @Serializable
    @SerialName("horizontalRule")
    data object HorizontalRuleBlock : PageBlock()

    @Serializable
    @SerialName("boxedText")
    // TODO is color a String, though?
    data class BoxedTextBlock(val backgroundColor: String, val data: String) : PageBlock()

    @Serializable
    @SerialName("chips")
    data class ChipsBlock(
        val multiSelect: Boolean,
        val savedName: String,
        val choices: List<String>
    ) : PageBlock()

    @Serializable
    @SerialName("accordion")
    data class AccordionBlock(
        val choices: List<AccordionChoice>,
        val defaultElements: List<PageBlock>
    ) : PageBlock()

    @Serializable
    @SerialName("input")
    // TODO is size a String, though?
    data class InputBlock(val size: String, val savedName: String, val watermark: String) : PageBlock()

    @Serializable
    @SerialName("navigationButton")
    data class NavigationButtonBlock(val text: String, val destination: Int) : PageBlock()

    @Serializable
    @SerialName("showIfSaved")
    data class ShowIfSavedBlock(val check: List<String>, val elements: List<PageBlock>) :
        PageBlock()
}