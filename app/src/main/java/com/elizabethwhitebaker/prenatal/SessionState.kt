package com.elizabethwhitebaker.prenatal

import com.elizabethwhitebaker.prenatal.session.Session
import com.elizabethwhitebaker.prenatal.session.SessionPage

data class SessionState(
    val session: Session,

    val page: SessionPage = session.pages.first(),

    val blockStates: Map<String, PageBlockState> = emptyMap(),

    val blockStatePages: Map<String, Int> = emptyMap(),

    val isListening: Boolean = false,

    val history: List<Int> = emptyList()
)