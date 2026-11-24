package com.elizabethwhitebaker.prenatal.views

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.elizabethwhitebaker.prenatal.PageBlockState
import com.elizabethwhitebaker.prenatal.SessionState
import com.elizabethwhitebaker.prenatal.session.Session
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SessionViewModel(session: Session) : ViewModel() {

    private val _uiState = MutableStateFlow(SessionState(session))
    val uiState: StateFlow<SessionState> = _uiState.asStateFlow()

    fun reset() {
        _uiState.update { currentState ->
            currentState.copy(
                page = currentState.session.pages.first(),
                history = emptyList(),
                blockStates = emptyMap(),
                blockStatePages = emptyMap(),
                isListening = false,
            )
        }
    }

    fun previousPage() {
        // UI is supposed to collect the state to an immutable snapshot, but I'd think the business logic is fine without?
        if (_uiState.value.history.isNotEmpty()) {
            _uiState.update { currentState ->
                val newPage = currentState.history.last()
                currentState.copy(
                    page = currentState.session.pages[newPage],
                    history = currentState.history.slice(0..<currentState.history.size - 1),
                    isListening = false,
                )
            }
        }
    }

    fun skip() {
        // I want to avoid code repetition, but a three-leg chain to avoid restating a number seems extreme
        goToPage(_uiState.value.page.pageNumber + 1, true)
    }

    fun nextPage() {
        goToPage(_uiState.value.page.pageNumber + 1)
    }

    fun goToPage(newPage: Int) {
        goToPage(newPage, false)
    }

    // Since the same logic should run with or without a skip, have a private "maybe-skip" goToPage
    private fun goToPage(newPage: Int, dropBlockStates: Boolean) {
        val newPageId = newPage - 1 // Pages are 1-indexed in the JSON

        _uiState.update { currentState ->
            // It appears easier to declare these mutable and then replace if necessary than deal with two if statements
            var newBlockStates = currentState.blockStates
            var newBlockStatePages = currentState.blockStatePages

            if (dropBlockStates) {
                // Ascertain which keys we're looking to remove...
                val filteredKeys =
                    newBlockStatePages.filter { it.value == currentState.page.pageNumber }.keys
                // ...then remove them from both maps at once
                newBlockStates = newBlockStates - filteredKeys
                newBlockStatePages = newBlockStatePages - filteredKeys
            }

            currentState.copy(
                history = currentState.history.plus(currentState.page.pageNumber - 1),
                page = currentState.session.pages[newPageId],
                blockStates = newBlockStates,
                blockStatePages = newBlockStatePages,
                isListening = false
            )
        }
    }

    // Dummy for demo purposes
    // TODO is this really the play?
    fun listen() {
        _uiState.update { currentState ->
            currentState.copy(
                isListening = !currentState.isListening
            )
        }
    }

    fun setBlockState(key: String, blockState: PageBlockState) {
        _uiState.update { currentState ->
            val currentPage = currentState.page.pageNumber
            currentState.copy(
                blockStates = currentState.blockStates.plus(Pair(key, blockState)),
                blockStatePages = currentState.blockStatePages.plus(Pair(key, currentPage))
            )
        }
    }

    // Define ViewModel factory in a companion object
    companion object {
        // Define a custom key using the factory function
        val SESSION_KEY = CreationExtras.Key<Session>()

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                // This is in the example but I don't know what it's for
                // val savedStateHandle = createSavedStateHandle()
                val session = this[SESSION_KEY] as Session
                SessionViewModel(
                    session = session,
                    //savedStateHandle = savedStateHandle
                )
            }
        }
    }

}