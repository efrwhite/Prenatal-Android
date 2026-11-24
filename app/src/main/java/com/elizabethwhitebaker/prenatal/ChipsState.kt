package com.elizabethwhitebaker.prenatal

class ChipsState(
    val choicesMap: Map<String, Boolean>
) : PageBlockState {

    constructor (choicesList: List<String>) : this(choicesList.associateWith { false })

    override val description: String
        // Reduces the dict to only ones that are true (e.g. selected) and then returns a concatenated string of any results
        get() = choicesMap.filter { it.value }.keys.joinToString(", ")

}