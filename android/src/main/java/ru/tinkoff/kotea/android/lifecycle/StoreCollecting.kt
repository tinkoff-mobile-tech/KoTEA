package ru.tinkoff.kotea.android.lifecycle

import androidx.core.app.ComponentActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.tinkoff.kotea.android.ui.ContextResourcesProvider
import ru.tinkoff.kotea.android.ui.UiStateMapper
import ru.tinkoff.kotea.android.ui.map
import ru.tinkoff.kotea.core.Store

/**
 * Subscribe to `state` and `news` of this [Store].
 *
 * `stateCollector` is collected between `onStart` and `onStop`,
 * `newsCollector` is collected between `onResume` and `onPause`.
 *
 * You can call this method either from:
 * - `onCreate` of `Activity` or `Fragment` with `lifecycleOwner = this`
 * - `onViewCreated` of `Fragment` with `lifecycleOwner = viewLifecycleOwner`
 */
fun <State : Any, News : Any> Store<State, *, News>.collectOnCreate(
    lifecycleOwner: LifecycleOwner,
    stateCollector: ((State) -> Unit)?,
    newsCollector: ((News) -> Unit)? = null
) = collectBuild(lifecycleOwner, { state }, stateCollector, news, newsCollector)

/**
 * @see [collectOnCreate]
 *
 * @param uiStateMapper - map your business state to ui state
 */
fun <State : Any, UiState : Any, News : Any> Store<State, *, News>.collectOnCreate(
    activity: ComponentActivity,
    uiStateMapper: UiStateMapper<State, UiState>,
    stateCollector: ((UiState) -> Unit)?,
    newsCollector: ((News) -> Unit)? = null
) {
    val mappedStateFlow = {
        val provider = ContextResourcesProvider(activity)
        state.map { uiStateMapper.map(provider, it) }
    }
    collectBuild(activity, mappedStateFlow, stateCollector, news, newsCollector)
}

/**
 * @see [collectOnCreate]
 *
 * @param uiStateMapper - map your business state to ui state
 */
fun <State : Any, UiState : Any, News : Any> Store<State, *, News>.collectOnCreate(
    fragment: Fragment,
    uiStateMapper: UiStateMapper<State, UiState>,
    stateCollector: ((UiState) -> Unit)?,
    newsCollector: ((News) -> Unit)? = null
) {
    val mappedStateFlow = {
        val provider = ContextResourcesProvider(fragment.requireContext())
        state.map { uiStateMapper.map(provider, it) }
    }
    collectBuild(fragment, mappedStateFlow, stateCollector, news, newsCollector)
}

private fun <State : Any, News : Any> collectBuild(
    lifecycleOwner: LifecycleOwner,
    stateFlow: () -> Flow<State>,
    stateCollector: ((State) -> Unit)?,
    news: Flow<News>,
    newsCollector: ((News) -> Unit)?,
) {
    val lifecycle = lifecycleOwner.lifecycle

    check(lifecycle.currentState == Lifecycle.State.INITIALIZED) { "Must be called before OnCreate" }

    with(lifecycleOwner.lifecycleScope) {
        if (stateCollector != null) {
            launch {
                stateFlow().flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                    .collect(stateCollector::invoke)
            }
        }

        if (newsCollector != null) {
            launch {
                news.flowWithLifecycle(lifecycle, Lifecycle.State.RESUMED)
                    .collect(newsCollector::invoke)
            }
        }
    }
}
