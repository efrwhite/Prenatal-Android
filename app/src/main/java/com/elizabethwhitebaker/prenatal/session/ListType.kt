package com.elizabethwhitebaker.prenatal.session

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class ListType {
    @SerialName("bullet")
    BULLET,

    @SerialName("decimal")
    DECIMAL,

    //@SerialName("lower_alphabetical")
    //LOWER_ALPHABETICAL,

    //@SerialName("upper_alphabetical")
    //UPPER_ALPHABETICAL,

    //@SerialName("lower_roman")
    //LOWER_ROMAN

    //@SerialName("upper_roman")
    //UPPER_ROMAN

    // @SerialName("character")
    // CHARACTER // Harder to implement

    @SerialName("none")
    NONE
}