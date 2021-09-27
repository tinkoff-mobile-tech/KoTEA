package ru.tinkoff.kotea.core.impl

import ru.tinkoff.kotea.core.CommandsFlowHandler

class CommandsFlowHandlerException(
    handlerClass: Class<out CommandsFlowHandler<*, *>>,
    cause: Throwable
) : RuntimeException("Exception in ${handlerClass.canonicalName}", cause)
