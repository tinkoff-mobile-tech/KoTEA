package ru.tinkoff.kotea.logging

import ru.tinkoff.kotea.core.Next
import ru.tinkoff.kotea.core.Update

internal class LoggingUpdate<State : Any, Event : Any, Command : Any, News : Any>(
    private val delegate: Update<State, Event, Command, News>,
    private val tag: String,
    private val logger: KoteaLogger,
) : Update<State, Event, Command, News> {

    override fun update(
        state: State,
        event: Event
    ): Next<State, Command, News> {
        val next = delegate.update(state, event)

        logger.debug(tag, "Event: ${event.toString().removePackage(delegate.javaClass)}")

        if (next.state != null) {
            logger.debug(tag, "State: ${next.state.toString().removePackage(delegate.javaClass)}")
        }

        if (next.commands.isNotEmpty()) {
            for (command in next.commands) {
                logger.debug(tag, "Command: ${command.toString().removePackage(delegate.javaClass)}")
            }
        }

        if (next.news.isNotEmpty()) {
            for (news in next.news) {
                logger.debug(tag, "News: ${news.toString().removePackage(delegate.javaClass)}")
            }
        }

        return next
    }
}
