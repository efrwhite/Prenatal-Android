package com.elizabethwhitebaker.prenatal.session.actionButton

import kotlinx.serialization.Serializable

@Serializable
class ActionButton(
    val type: ActionButtonType,
    val text: String,
    val destination: Int? = null,
    val customText: String? = null,
    val customDestination: Int? = null,
    val subscript: String? = null
)