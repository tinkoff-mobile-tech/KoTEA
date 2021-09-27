package ru.tinkoff.kotea.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface Store<out State, in Event, out News> {

    /**
     * State of this screen.
     * At every moment in time complete screen state can be recreated by just the data stored in it.
     * You **should** make exceptions if it complicates your screen logic
     * (usually when you trying to send some event from the UI immediately after receiving a new state,
     * use [news] in that case).
     *
     * Can be [collected][Flow.collect] or used to retrieve current state via state.[value][StateFlow.value].
     * It is recommended to collect it only when screen is visible (e.g. between `onStart` and `onStop` on Android).
     */
    val state: StateFlow<State>

    /**
     * One-off commands to trigger some UI side effects (e.g. error dialog).
     *
     * It is recommended to collect them between `onResume` and `onPause`.
     * News that are emitted when there is no subscriptions will be cached and delivered to a first collector.
     */
    val news: Flow<News>

    /**
     * Dispatch the UI [event] to this store. It will be cached and delivered even if store is not launched yet.
     */
    fun dispatch(event: Event)

    /**
     * Launches this store in the specified [coroutineScope].
     *
     * When using in `ViewModel` it is recommended to use either:
     * - `viewModelScope + Dispatchers.Unconfined` (similar to RxJava without using scheduling operators –
     *   minimizes thread switching, update and command handlers can be invoked from any thread, including main)
     * - `viewModelScope + Dispatchers.Default` (
     *
     * Can only be invoked once.
     */
    fun launchIn(coroutineScope: CoroutineScope)
}
