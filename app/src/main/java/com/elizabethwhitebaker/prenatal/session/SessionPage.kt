package com.elizabethwhitebaker.prenatal.session

import com.elizabethwhitebaker.prenatal.session.actionButton.ActionButton
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SessionPage(
    val pageNumber: Int,
    val audio: String,
    val progress: Double,
    @SerialName(SessionSerializer.BLOCKS_SERIAL_NAME) // Redundant in theory, but ensures consistency
    val blocks: List<PageBlock>,
    val actionButton: ActionButton
)