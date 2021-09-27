package ru.tinkoff.mobile.kotea.sample.analytics

import android.util.Log
import ru.tinkoff.mobile.kotea.sample.CounterEvent
import ru.tinkoff.mobile.kotea.sample.CounterEvent.CounterCommandsResultEvent.CounterTick
import ru.tinkoff.mobile.kotea.sample.CounterEvent.CounterUiEvent
import ru.tinkoff.mobile.kotea.sample.CounterEvent.CounterUiEvent.ResetClicked
import ru.tinkoff.mobile.kotea.sample.CounterEvent.CounterUiEvent.StartClicked
import ru.tinkoff.mobile.kotea.sample.CounterEvent.CounterUiEvent.StopClicked
import ru.tinkoff.mobile.kotea.sample.CounterState

class AnalyticsTracker {

    operator fun invoke(state: CounterState, event: CounterEvent) {
        when (event) {
            CounterTick -> Unit
            is CounterUiEvent -> trackUiEvent(state, event)
        }
    }

    private fun trackUiEvent(state: CounterState, event: CounterUiEvent) {
        when (event) {
            ResetClicked -> Log.d("CounterAnalytics", "Counter reset")
            StartClicked -> Log.d("CounterAnalytics", "Counter started with value: ${state.count}")
            StopClicked -> Log.d("CounterAnalytics", "Counter stopped with value: ${state.count}")
        }
    }
}