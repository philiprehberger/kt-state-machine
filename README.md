# state-machine

[![Tests](https://github.com/philiprehberger/kt-state-machine/actions/workflows/publish.yml/badge.svg)](https://github.com/philiprehberger/kt-state-machine/actions/workflows/publish.yml)
[![Maven Central](https://img.shields.io/maven-central/v/com.philiprehberger/state-machine.svg)](https://central.sonatype.com/artifact/com.philiprehberger/state-machine)
[![Last updated](https://img.shields.io/github/last-commit/philiprehberger/kt-state-machine)](https://github.com/philiprehberger/kt-state-machine/commits/main)

Type-safe finite state machine with DSL, transition guards, and side effects.

## Installation

### Gradle (Kotlin DSL)

```kotlin
implementation("com.philiprehberger:state-machine:0.2.0")
```

### Maven

```xml
<dependency>
    <groupId>com.philiprehberger</groupId>
    <artifactId>state-machine</artifactId>
    <version>0.2.0</version>
</dependency>
```

## Usage

```kotlin
import com.philiprehberger.statemachine.*

sealed class State { object Idle : State(); object Running : State() }
sealed class Event { object Start : Event(); object Stop : Event() }

val sm = stateMachine<State, Event>(State.Idle) {
    state<State.Idle> { on<Event.Start> { transitionTo(State.Running) } }
    state<State.Running> { on<Event.Stop> { transitionTo(State.Idle) } }
}

sm.send(Event.Start) // currentState = Running
sm.canSend(Event.Stop) // true
```

### Transition History

```kotlin
sm.send(Event.Start)
sm.send(Event.Stop)

sm.history().forEach { t ->
    println("${t.from} --[${t.event}]--> ${t.to}")
}
```

### Available Events

```kotlin
val events = sm.availableEvents()
// Returns the set of event classes valid for the current state
```

### Reset

```kotlin
sm.reset() // Returns to initial state, clears history
```

## API

| Function / Class | Description |
|------------------|-------------|
| `stateMachine(initial) { }` | DSL to define a state machine |
| `state<S> { on<E> { } }` | Define transitions for a state |
| `transitionTo(state)` | Set the target state |
| `guard { s, e -> Boolean }` | Conditional transition |
| `sideEffect { from, event, to -> }` | Execute on transition |
| `StateMachine.send(event)` | Trigger a state transition |
| `StateMachine.canSend(event)` | Check if transition is valid |
| `StateMachine.currentState` | Current state |
| `StateMachine.history()` | Get list of all transitions made |
| `StateMachine.availableEvents()` | Get valid events for current state |
| `StateMachine.reset()` | Reset to initial state, clear history |
| `Transition<S, E>` | Records from, event, to, and timestamp |

## Development

```bash
./gradlew test
./gradlew build
```

## Support

If you find this project useful:

⭐ [Star the repo](https://github.com/philiprehberger/kt-state-machine)

🐛 [Report issues](https://github.com/philiprehberger/kt-state-machine/issues?q=is%3Aissue+is%3Aopen+label%3Abug)

💡 [Suggest features](https://github.com/philiprehberger/kt-state-machine/issues?q=is%3Aissue+is%3Aopen+label%3Aenhancement)

❤️ [Sponsor development](https://github.com/sponsors/philiprehberger)

🌐 [All Open Source Projects](https://philiprehberger.com/open-source-packages)

💻 [GitHub Profile](https://github.com/philiprehberger)

🔗 [LinkedIn Profile](https://www.linkedin.com/in/philiprehberger)

## License

[MIT](LICENSE)
