package com.philiprehberger.statemachine

import kotlin.reflect.KClass

/** A type-safe finite state machine. */
public class StateMachine<S : Any, E : Any> internal constructor(
    initial: S,
    private val transitions: Map<Pair<KClass<out S>, KClass<out E>>, TransitionDef<S, E>>,
    private val listener: ((S, E, S) -> Unit)?,
) {
    @Volatile
    public var currentState: S = initial
        private set

    /** Send an event, triggering a state transition. */
    public fun send(event: E) {
        val key = Pair(currentState::class, event::class)
        val def = transitions[key] ?: throw IllegalStateException("No transition from ${currentState::class.simpleName} on ${event::class.simpleName}")
        if (def.guard != null && !def.guard.invoke(currentState, event)) {
            throw IllegalStateException("Guard rejected transition from ${currentState::class.simpleName} on ${event::class.simpleName}")
        }
        val from = currentState
        currentState = def.target
        def.sideEffect?.invoke(from, event, def.target)
        listener?.invoke(from, event, def.target)
    }

    /** Check if an event can be sent from the current state. */
    public fun canSend(event: E): Boolean {
        val key = Pair(currentState::class, event::class)
        val def = transitions[key] ?: return false
        return def.guard?.invoke(currentState, event) ?: true
    }
}

internal data class TransitionDef<S : Any, E : Any>(
    val target: S,
    val guard: ((S, E) -> Boolean)?,
    val sideEffect: ((S, E, S) -> Unit)?,
)
