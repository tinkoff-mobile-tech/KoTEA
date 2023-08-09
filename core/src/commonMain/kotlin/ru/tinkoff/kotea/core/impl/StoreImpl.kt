package ru.tinkoff.kotea.core.impl

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import ru.tinkoff.kotea.core.CommandsFlowHandler
import ru.tinkoff.kotea.core.Store
import ru.tinkoff.kotea.core.Update
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Kotea store implementation
 *
 * @param initialCommands commands for initial emission.
 * Important: the list will be converted to a cold flow, so the commands will be re-emitted on each collection.
 **/
internal class StoreImpl<State : Any, Event : Any, UiEvent : Event, Command : Any, News : Any>(
    initialState: State,
    private val initialCommands: List<Command> = emptyList(),
    private val commandsFlowHandlers: List<CommandsFlowHandler<Command, Event>>,
    private val update: Update<State, Event, Command, News>,
) : Store<State, UiEvent, News> {

    override var state = MutableStateFlow(initialState)
        private set

    private val eventChannel = Channel<Event>(capacity = Channel.UNLIMITED)
    private val commandChannel = Channel<Command>(capacity = Channel.UNLIMITED)
    private val newsChannel = Channel<News>(capacity = Channel.UNLIMITED)

    private val isLaunched = AtomicBoolean(false)

    override fun launchIn(coroutineScope: CoroutineScope) {
        if (isLaunched.getAndSet(true)) error("Store has already been launched")

        val sharedCommands: Flow<Command> = commandChannel
            .consumeAsFlow()
            .shareIn(coroutineScope, SharingStarted.Eagerly)

        val commandsFlow = if (initialCommands.isNotEmpty()) {
            merge(sharedCommands, initialCommands.asFlow())
        } else {
            sharedCommands
        }

        for (flowHandler in commandsFlowHandlers) {
            coroutineScope.launch(start = CoroutineStart.UNDISPATCHED) {
                flowHandler.handle(commandsFlow)
                    .catch { throwable ->
                        if (throwable is CancellationException) throw throwable
                        else throw CommandsFlowHandlerException(flowHandler::class.java, throwable)
                    }
                    .collect(eventChannel::send)
            }
        }

        coroutineScope.launch {
            while (isActive) {
                val event = eventChannel.receive()
                val next = update.update(state.value, event)
                if (next.state != null) {
                    state.value = next.state
                }
                for (command in next.commands) {
                    commandChannel.send(command)
                }
                for (news in next.news) {
                    newsChannel.send(news)
                }
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    override val news: Flow<News> = newsChannel
        .receiveAsFlow()
        .shareIn(GlobalScope, SharingStarted.WhileSubscribed())

    override fun dispatch(event: UiEvent) {
        eventChannel.trySend(event)
    }
}
