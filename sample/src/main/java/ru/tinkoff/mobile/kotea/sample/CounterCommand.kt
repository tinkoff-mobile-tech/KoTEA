package ru.tinkoff.mobile.kotea.sample

sealed interface CounterCommand {

    object Start : CounterCommand

    object Stop : CounterCommand
}