package com.philiprehberger.statemachine

import kotlin.test.*

sealed class GState { object A : GState(); object B : GState() }
sealed class GEvent { data class Go(val allowed: Boolean) : GEvent() }

class GuardTest {
    @Test fun `guard blocks`() {
        val sm = stateMachine<GState, GEvent>(GState.A) {
            state<GState.A> { on<GEvent.Go> { transitionTo(GState.B); guard { _, e -> (e as GEvent.Go).allowed } } }
        }
        assertFailsWith<IllegalStateException> { sm.send(GEvent.Go(false)) }
        assertEquals(GState.A, sm.currentState)
    }
    @Test fun `guard allows`() {
        val sm = stateMachine<GState, GEvent>(GState.A) {
            state<GState.A> { on<GEvent.Go> { transitionTo(GState.B); guard { _, e -> (e as GEvent.Go).allowed } } }
        }
        sm.send(GEvent.Go(true))
        assertEquals(GState.B, sm.currentState)
    }
}
