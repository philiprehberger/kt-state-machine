package com.philiprehberger.statemachine

import kotlin.reflect.KClass

/** Build a state machine with a DSL. */
public fun <S : Any, E : Any> stateMachine(initial: S, block: StateMachineBuilder<S, E>.() -> Unit): StateMachine<S, E> {
    val builder = StateMachineBuilder<S, E>()
    builder.block()
    return StateMachine(initial, builder.transitions, builder.listener)
}

public class StateMachineBuilder<S : Any, E : Any> {
    internal val transitions = mutableMapOf<Pair<KClass<out S>, KClass<out E>>, TransitionDef<S, E>>()
    internal var listener: ((S, E, S) -> Unit)? = null

    /** Define transitions for a state. */
    public inline fun <reified ST : S> state(block: StateBuilder<S, E, ST>.() -> Unit) {
        val sb = StateBuilder<S, E, ST>(ST::class)
        sb.block()
        transitions.putAll(sb.transitions)
    }

    /** Global transition listener. */
    public fun onTransition(block: (S, E, S) -> Unit) { listener = block }
}

public class StateBuilder<S : Any, E : Any, ST : S>(private val stateClass: KClass<ST>) {
    internal val transitions = mutableMapOf<Pair<KClass<out S>, KClass<out E>>, TransitionDef<S, E>>()

    /** Define a transition on an event. */
    public inline fun <reified EV : E> on(block: TransitionBuilder<S, E>.() -> Unit) {
        val tb = TransitionBuilder<S, E>()
        tb.block()
        transitions[Pair(stateClass, EV::class)] = TransitionDef(tb.target!!, tb.guard, tb.sideEffect)
    }
}

public class TransitionBuilder<S : Any, E : Any> {
    internal var target: S? = null
    internal var guard: ((S, E) -> Boolean)? = null
    internal var sideEffect: ((S, E, S) -> Unit)? = null

    /** Set the target state. */
    public fun transitionTo(state: S) { target = state }
    /** Add a guard condition. */
    public fun guard(block: (S, E) -> Boolean) { guard = block }
    /** Add a side effect. */
    public fun sideEffect(block: (S, E, S) -> Unit) { sideEffect = block }
}
