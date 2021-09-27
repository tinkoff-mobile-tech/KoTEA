package ru.tinkoff.mobile.kotea.sample

sealed interface CounterNews {

    data class ShowToast(
        val text: String,
    ) : CounterNews
}