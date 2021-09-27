package ru.tinkoff.kotea.core

fun interface UncaughtExceptionHandler {

    fun handle(throwable: Throwable)
}
