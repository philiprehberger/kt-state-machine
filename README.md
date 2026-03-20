# state-machine

[![CI](https://github.com/philiprehberger/kt-state-machine/actions/workflows/publish.yml/badge.svg)](https://github.com/philiprehberger/kt-state-machine/actions/workflows/publish.yml)
[![Maven Central](https://img.shields.io/maven-central/v/com.philiprehberger/state-machine)](https://central.sonatype.com/artifact/com.philiprehberger/state-machine)
[![License](https://img.shields.io/github/license/philiprehberger/kt-state-machine)](LICENSE)

Type-safe finite state machine with DSL, transition guards, and side effects.

## Installation

### Gradle (Kotlin DSL)

```kotlin
dependencies {
    implementation("com.philiprehberger:state-machine:0.1.4")
}
```

### Maven

```xml
<dependency>
    <groupId>com.philiprehberger</groupId>
    <artifactId>state-machine</artifactId>
    <version>0.1.4</version>
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

## Development

```bash
./gradlew test
./gradlew build
```

## License

MIT
