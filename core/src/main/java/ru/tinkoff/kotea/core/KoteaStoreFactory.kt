package ru.tinkoff.kotea.core

import ru.tinkoff.kotea.core.impl.StoreImpl

/**
 * Create a new TEA [Store].
 *
 * How it works:
 * ```
 *
 *            UiEvent                         State Λ   Λ News
 *  View        │                                   │   │
 * -------------│-----------------------------------│---│------
 *              │    ┌──────────────<───────────────┤   │
 *              │    │                              │   │
 *              │    │        ╭────────────╮ State  │   │
 *              │    │  State │            ├────────┘   │
 *              V    └────────>            │ News       │
 *              │       Event │   Update   ├────────────┘
 *  Store       ├─────────────>            │ Commands
 *              │             │            ├───────────────┐
 *              Λ             ╰────────────╯               │
 *              │                                          │
 *              │         ╭─────────────────────╮          │
 *              │        ╭┴────────────────────╮│          │
 *              │ Events │                     ││ Commands │
 *              └────────┤ CommandsFlowHandler <───────────┘
 *                       │                     ├╯
 *                       ╰────────Λ───┬────────╯
 * -------------------------------│---│------------------------
 *  Model                         │   │
 *               Data (or events) ┘   V Side effects
 * ```
 *
 * @see Update
 * @see CommandsFlowHandler
 * @see StoreImpl
 */
fun <State : Any, Event : Any, UiEvent : Event, Command : Any, News : Any> KoteaStore(
    initialState: State,
    initialCommands: List<Command> = emptyList(),
    commandsFlowHandlers: List<CommandsFlowHandler<Command, Event>> = emptyList(),
    update: Update<State, Event, Command, News> = Update { _, _ -> Next() },
    uncaughtExceptionHandler: UncaughtExceptionHandler = UncaughtExceptionHandler { }
): Store<State, UiEvent, News> {
    return StoreImpl(initialState, initialCommands, commandsFlowHandlers, update, uncaughtExceptionHandler)
}
