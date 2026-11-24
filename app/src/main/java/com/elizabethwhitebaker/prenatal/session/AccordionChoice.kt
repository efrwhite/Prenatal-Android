package com.elizabethwhitebaker.prenatal.session

import kotlinx.serialization.Serializable

@Serializable
data class AccordionChoice(
    val icon: String,
    val label: String,
    val elements: List<PageBlock>
)