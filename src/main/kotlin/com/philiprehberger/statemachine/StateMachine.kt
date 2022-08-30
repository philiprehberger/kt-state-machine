package com.philiprehberger.statemachine

import kotlin.reflect.KClass

/**
 * Represents a recorded state transition.
 *
 * @param S The state type.
 * @param E The event type.
 * @property from The state before the transition.
 * @property event The event that triggered the transition.
 * @property to The state after the transition.
 * @property timestamp The epoch millisecond timestamp of the transition.
 */
public data class Transition<S, E>(
    public val from: S,
    public val event: E,
    public val to: S,
    public val timestamp: Long = System.currentTimeMillis(),
)

/** A type-safe finite state machine with transition history. */
public class StateMachine<S : Any, E : Any> internal constructor(
    private val initial: S,
    private val transitions: Map<Pair<KClass<out S>, KClass<out E>>, TransitionDef<S, E>>,
    private val listener: ((S, E, S) -> Unit)?,
) {
    @Volatile
    public var currentState: S = initial
        private set

    private val _history = mutableListOf<Transition<S, E>>()

    /** Send an event, triggering a state transition. */
    public fun send(event: E) {
        val key = Pair(currentState::class, event::class)
        val def = transitions[key] ?: throw IllegalStateException("No transition from ${currentState::class.simpleName} on ${event::class.simpleName}")
        if (def.guard != null && !def.guard.invoke(currentState, event)) {
            throw IllegalStateException("Guard rejected transition from ${currentState::class.simpleName} on ${event::class.simpleName}")
        }
        val from = currentState
        currentState = def.target
        _history.add(Transition(from, event, def.target))
        def.sideEffect?.invoke(from, event, def.target)
        listener?.invoke(from, event, def.target)
    }

    /** Check if an event can be sent from the current state. */
    public fun canSend(event: E): Boolean {
        val key = Pair(currentState::class, event::class)
        val def = transitions[key] ?: return false
        return def.guard?.invoke(currentState, event) ?: true
    }

    /**
     * Returns the history of all transitions made.
     *
     * @return An immutable list of [Transition] records in chronological order.
     */
    public fun history(): List<Transition<S, E>> = _history.toList()

    /**
     * Returns the set of event classes that have valid transitions from the current state.
     *
     * @return A set of [KClass] representing valid events for the current state.
     */
    public fun availableEvents(): Set<KClass<out E>> {
        return transitions.keys
            .filter { (stateClass, _) -> stateClass == currentState::class }
            .map { (_, eventClass) -> eventClass }
            .toSet()
    }

    /**
     * Resets the state machine to its initial state and clears the transition history.
     */
    public fun reset() {
        currentState = initial
        _history.clear()
    }
}

@PublishedApi internal data class TransitionDef<S : Any, E : Any>(
    val target: S,
    val guard: ((S, E) -> Boolean)?,
    val sideEffect: ((S, E, S) -> Unit)?,
)
