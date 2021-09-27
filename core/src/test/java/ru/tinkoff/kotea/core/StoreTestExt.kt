package ru.tinkoff.kotea.core

import app.cash.turbine.ReceiveTurbine
import app.cash.turbine.test
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.job
import kotlinx.coroutines.plus
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue

fun testStore(
    initialState: State,
    commandsHandlers: List<CommandsFlowHandler<Command, Event>> = emptyList(),
    update: Update<State, Event, Command, News> = Update { _, _ -> Next() },
    block: suspend Store<State, UiEvent, News>.() -> Unit
) {
    runBlocking {
        val store = KoteaStore(
            initialState = initialState,
            commandsFlowHandlers = commandsHandlers,
            update = update
        )
        val job = Job(coroutineContext.job)
        store.launchIn(this + job)
        store.block()
        job.cancelAndJoin()
    }
}

suspend fun Store<State, UiEvent, News>.assert(
    block: suspend (state: ReceiveTurbine<State>, news: ReceiveTurbine<News>) -> Unit
) {
    state.test {
        val stateTurbine = this
        news.test {
            block(stateTurbine, this)
            val remainingEvents = cancelAndConsumeRemainingEvents()
            assertTrue("Unconsumed news left: $remainingEvents", remainingEvents.isEmpty())
        }
        cancel()
    }
}

data class State(val name: String) {
    companion object {
        val initial = State("initial")
        val changed = State("changed")
    }

    override fun toString() = name
}

open class Event(val name: String) {
    companion object {
        val smthLoaded = UiEvent("smthLoaded")
    }

    override fun toString() = name
}

class UiEvent(name: String) : Event(name) {
    companion object {
        val smthClicked = UiEvent("smthClicked")
    }

    override fun toString() = name
}

open class Command(val name: String) {
    companion object {
        val loadSmth = Command("loadSmth")
    }

    override fun toString() = name
}

class News(name: String) : Command(name) {
    companion object {
        val hello = News("hello")
    }

    override fun toString() = name
}
