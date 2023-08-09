package ru.tinkoff.kotea.core

import ru.tinkoff.kotea.core.dsl.DslUpdate

/**
 * ```
 *       ╭────────────╮ State
 * State │            ├───>
 *   ────>            │ List<Command>
 * Event │   Update   ├───>
 *   ────>            │ List<News>
 *       │            ├───>
 *       ╰────────────╯
 * ```
 *
 * This "function" should contain as much as possible of screen (feature) logic but nothing else.
 * It should be implemented as a **pure** function,
 * which calculates a new [State] based only on a current [State] and a new [Event].
 *
 * But changing a [State] isn't enough to do something useful. So you can also return:
 * - [Command]s, which can be handled in a [CommandsFlowHandler] to trigger some side effects
 * - [News], which is one-off commands for UI
 *
 * For convenience, you can use [DslUpdate] (recommended).
 */
fun interface Update<State : Any, in Event : Any, out Command : Any, out News : Any> {

    /** @see Update */
    fun update(state: State, event: Event): Next<State, Command, News>
}
