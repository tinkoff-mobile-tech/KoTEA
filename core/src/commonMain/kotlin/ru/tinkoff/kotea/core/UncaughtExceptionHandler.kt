package ru.tinkoff.kotea.core

@Deprecated("For handling exceptions please use CoroutinesExceptionHandler")
fun interface UncaughtExceptionHandler {

    fun handle(throwable: Throwable)
}
