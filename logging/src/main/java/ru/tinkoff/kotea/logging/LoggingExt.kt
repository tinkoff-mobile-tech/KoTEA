package ru.tinkoff.kotea.logging

internal fun <T : Any> String.removePackage(clazz: Class<T>): String = removePrefix("${clazz.`package`?.name}.")
