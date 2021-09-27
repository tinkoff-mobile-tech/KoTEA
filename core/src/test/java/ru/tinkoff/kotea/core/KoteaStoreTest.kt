package ru.tinkoff.kotea.core

import org.junit.Assert.assertEquals
import org.junit.Test

class KoteaStoreTest {
    @Test
    fun `GIVEN empty store THEN state is initial`() = testStore(
        initialState = State.initial
    ) {
        assert { state, _ ->
            assertEquals(State.initial, state.awaitItem())
        }
    }

    @Test
    fun `GIVEN store with update WHEN dispatch THEN state changed and news emitted`() = testStore(
        initialState = State.initial,
        update = { _, event ->
            assertEquals(UiEvent.smthClicked, event)
            Next(state = State.changed, news = listOf(News.hello))
        }
    ) {
        assert { state, news ->
            state.awaitItem() // consume initial state
            dispatch(UiEvent.smthClicked)
            assertEquals(State.changed, state.awaitItem())
            assertEquals(News.hello, news.awaitItem())
        }
    }
}
