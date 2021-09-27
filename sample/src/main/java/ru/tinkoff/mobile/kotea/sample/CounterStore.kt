package ru.tinkoff.mobile.kotea.sample

import ru.tinkoff.kotea.core.Store
import ru.tinkoff.kotea.logging.KoteaLoggingStore
import ru.tinkoff.mobile.kotea.sample.analytics.AnalyticsTracker

class CounterStore : Store<CounterState, CounterEvent, CounterNews> by KoteaLoggingStore(
    initialState = CounterState(count = 0, isProgress = false),
    update = CounterUpdate(AnalyticsTracker()),
    commandsFlowHandlers = listOf(CounterCommandsFlowHandler())
)