package com.elizabethwhitebaker.prenatal.session.actionButton

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class ActionButtonType {
    @SerialName("next")
    NEXT,

    @SerialName("skipOrNext")
    SKIP_OR_NEXT,

    @SerialName("customOrNext")
    CUSTOM_OR_NEXT,

    @SerialName("custom")
    CUSTOM,

    @SerialName("finish")
    FINISH
}