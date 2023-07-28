package ru.tinkoff.kotea.logging

import ru.tinkoff.kotea.core.CommandsFlowHandler
import ru.tinkoff.kotea.core.KoteaStore
import ru.tinkoff.kotea.core.Next
import ru.tinkoff.kotea.core.Store
import ru.tinkoff.kotea.core.UncaughtExceptionHandler
import ru.tinkoff.kotea.core.Update

/**
 * Factory for logging store, which prints updates for states, commands, events and news via logger.
 */
fun <State : Any, Event : Any, UiEvent : Event, Command : Any, News : Any> KoteaLoggingStore(
    initialState: State,
    initialCommands: List<Command> = emptyList(),
    commandsFlowHandlers: List<CommandsFlowHandler<Command, Event>> = emptyList(),
    update: Update<State, Event, Command, News> = Update { _, _ -> Next() },
    tag: String = update.javaClass.simpleName,
    logger: KoteaLogger = DefaultKoteaLogger(),
): Store<State, UiEvent, News> {

    logger.debug(tag, "Initial State: ${initialState.toString().removePackage(update.javaClass)}")

    if (initialCommands.isNotEmpty()) {
        logger.debug(tag, "=== Initial Commands ===")
        for (command in initialCommands) {
            logger.debug(tag, "Command: ${command.toString().removePackage(update.javaClass)}")
        }
    }

    logger.debug(tag, "=== End of Initial Commands ===")

    return KoteaStore(
        initialState,
        initialCommands,
        commandsFlowHandlers,
        LoggingUpdate(
            delegate = update,
            tag = tag,
            logger = logger,
        ),
    )
}

@Deprecated("Use factory without uncaughtExceptionHandler (don't calling rigt now)")
fun <State : Any, Event : Any, UiEvent : Event, Command : Any, News : Any> KoteaLoggingStore(
    initialState: State,
    initialCommands: List<Command> = emptyList(),
    commandsFlowHandlers: List<CommandsFlowHandler<Command, Event>> = emptyList(),
    update: Update<State, Event, Command, News> = Update { _, _ -> Next() },
    uncaughtExceptionHandler: UncaughtExceptionHandler = UncaughtExceptionHandler { },
    tag: String = update.javaClass.simpleName,
    logger: KoteaLogger = DefaultKoteaLogger(),
): Store<State, UiEvent, News> {

    logger.debug(tag, "Initial State: ${initialState.toString().removePackage(update.javaClass)}")

    if (initialCommands.isNotEmpty()) {
        logger.debug(tag, "=== Initial Commands ===")
        for (command in initialCommands) {
            logger.debug(tag, "Command: ${command.toString().removePackage(update.javaClass)}")
        }
    }

    logger.debug(tag, "=== End of Initial Commands ===")

    return KoteaStore(
        initialState,
        initialCommands,
        commandsFlowHandlers,
        LoggingUpdate(
            delegate = update,
            tag = tag,
            logger = logger,
        ),
    )
}
