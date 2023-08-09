package ru.tinkoff.kotea.core

/** This class represents the result of calling an [Update] function */
class Next<out State : Any, out Command : Any, out News : Any>(
    /** New state to use (if set) */
    val state: State? = null,
    /** Commands which can be handled in a [CommandsFlowHandler] to trigger some side effects */
    val commands: List<Command> = emptyList(),
    /** One-off commands for UI (e.g. `ShowErrorDialog`) */
    val news: List<News> = emptyList(),
)
