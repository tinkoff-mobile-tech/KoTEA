package ru.tinkoff.mobile.kotea.sample

import ru.tinkoff.kotea.android.ui.ResourcesProvider
import ru.tinkoff.kotea.android.ui.UiStateMapper

class CounterUiStateMapper : UiStateMapper<CounterState, CounterUiState> {

    override fun ResourcesProvider.map(state: CounterState): CounterUiState {
        val progressTextRes: Int = if (state.isProgress) {
            R.string.counter_started
        } else {
            R.string.counter_stopped
        }

        return CounterUiState(
            countText = getString(R.string.counter_title, state.count),
            progressText = getString(progressTextRes)
        )
    }
}