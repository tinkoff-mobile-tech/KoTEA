package ru.tinkoff.kotea.core

import kotlinx.coroutines.flow.Flow

/**
 * ```
 * Flow<Command> ╭─────────────────────╮ Flow<Event>
 *       ────────> CommandsFlowHandler ├───────>
 *               ╰────────Λ───┬────────╯
 *                        ╵   V
 *                        Model
 * ```
 */
fun interface CommandsFlowHandler<in Command : Any, out Event : Any> {

    /**
     * Flow can be collected from an any thread, including main.
     * So you should always offload any expensive operations to a background threads.
     */
    fun handle(commands: Flow<Command>): Flow<Event>
}
