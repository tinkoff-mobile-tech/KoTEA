package ru.tinkoff.kotea.android.ui

interface UiStateMapper<State : Any, UiState : Any> {
    fun ResourcesProvider.map(state: State): UiState
}

fun <State : Any, UiState : Any> UiStateMapper<State, UiState>.map(
    resourcesProvider: ResourcesProvider,
    state: State
): UiState {
    return resourcesProvider.map(state)
}
