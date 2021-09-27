package ru.tinkoff.kotea.logging

internal class DefaultKoteaLogger : KoteaLogger {

    override fun debug(tag: String, message: String) {
        println("$tag : $message")
    }
}