# Kotea

Kotlin TEA reactive implementation by Tinkoff

## Introduction

This architectural approach is based on the ideas of TEA (The Elm Architecture) and aims to replace RxRedux
solving its most obvious problems.

Kotea is designed to implement the "Presentation" layer in the classic "Model + Presentation + View" app layers model.
Let's call this presentation unit "KoteaStore" or just "Store".

## How to use

```groovy
implementation("ru.tinkoff.kotea:core:<version>")
implementation("ru.tinkoff.kotea:android:<version>") // android extensions
implementation("ru.tinkoff.kotea:logging:<version>") // factory with logging feature
```

## Layers model

<img src="/docs/media/kotea_scheme.png" width="668" alt="kotea scheme"/>

## Main Components

### State

`State` is a class that can describe the state of your app/feature/screen/view/etc at a certain point in time.

```kotlin
data class State(
    val title: String,
    val description: String,
    val items: List<Int>,
)
```

### News

`News` are Single Live Events that your store can produce during its lifetime. It represents single events that should
be handled only once,
e.g. showing toast/dialog, opening new activity, playing vibration, etc.

```kotlin
sealed interface News {

    class NavigateTo(screen: Screen) : News

    class ShowErrorToast(text: String) : News
}
```

### Commands

Any `Command` represents an intention of invoking part of your business logic. Commands are sent from the `Update`
depending on the current state and the received event.

```kotlin
sealed interface Command {

    class LoadData(val page: Int = 0) : Command

    object RefreshData : Command
}
```

### Update

The `Update` class is a place for your presentation logic. Its `update` method accepts `state` and `event` arguments and
returns `Next`, which contains new state, news and commands to be sent. The idea of `Update` is to be a pure function
that modifies the state and sends commands and news according to the received event and the current state.

```kotlin

class YourUpdate : Update<State, Event, Command, News> {

    override fun update(state: State, event: Event): Next<State, Command, News> {
        return Next(
            state = state.copy(title = "New Title")
        )
    }
}
```

To map your state in a more convenient way you can use `DslUpdate` with DSL syntax:

```kotlin
class YourUpdate : DslUpdate<State, Event, Command, News>() {

    override fun NextBuilder.update(event: Event): Next<State, Command, News> {
        state { copy(title = "New Title") }
        commands(Command.Start)
        news(News.ShowToast(text = "It is Tinkoff"))
    }
}
```

### Store

Store is the heart of framework. It is routing IU events, events, news and commands to subscriber and commands handler.
You can implement your own store, but we recommend using the `KoteaStore` function. It accepts the following params:

- `initial state` - start state of your screen
- `initial commands` - commands that will be send immediately after start
- `commands flow handlers` - the executors for your commands
- `update` - update that will use for your presentation logic
- `uncaught exception handler` - exception handler for commands handlers

## Android extensions

To simplify lifecycle management along subscribing to a store you can use functions from an extension library for
android. It contains a set of functions for collecting state flow and handling news according to the View's lifecycle.

```kotlin
class YourActivity : Activity(R.layout.your_activity_layout) {

    private val store by storeViaViewModel { YoutStore() }

    override fun onCreate(savedInstanceState: Bundle?) {
        store.collectOnCreate(
            activity = this,
            stateCollector = ::collect,
            newsCollector = ::handle
        )
    }

    private fun collect(state: State) {
        // omitted code
    }

    private fun handle(news: News) {
        // omitted code
    }
}
```

Alternatively you can use version that uses a `UiStateMapper`. `UiStateMapper` is a class for mapping your
presentation-layer `State` to the `UiState`, a class that directly describes your screen's content.

`UiStateMapper` has shorter lifecycle than a Store itself. Its implementations can use reference to `ResourceProvider`,
which provides up-to-date resources.

```kotlin
class YourUiStateMapper : UiStateMapper<State, UiState> {

    override fun ResourcesProvider.map(state: State): UiState {
        return UiState(
            title = getString(R.string.title),
            description = getString(R.string.description),
            content = getString(R.string.content, state.countValue)
        )
    }
}

class YourActivity : Activity(R.layout.your_activity_layout) {

    private val store by storeViaViewModel { YoutStore() }

    override fun onCreate(savedInstanceState: Bundle?) {
        store.collectOnCreate(
            activity = this,
            uiStateMapper = YourUiStateMapper(),
            stateCollector = ::collect,
            newsCollector = ::handle
        )
    }

    private fun collect(state: UiState) {
        // omitted code
    }

    private fun handle(news: News) {
        // omitted code
    }
}
```

## Logging feature

To debug your screen, you can use logging feature by creating your store via `KoteaLoggingStore` function from `logging`
artifact.
In Logcat you will see logs with your events, news, commands and state. 
