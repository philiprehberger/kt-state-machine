package com.philiprehberger.statemachine

import kotlin.test.*

class SideEffectTest {
    @Test fun `side effect executes`() {
        var executed = false
        val sm = stateMachine<GState, GEvent>(GState.A) {
            state<GState.A> { on<GEvent.Go> { transitionTo(GState.B); sideEffect { _, _, _ -> executed = true } } }
        }
        sm.send(GEvent.Go(true))
        assertTrue(executed)
    }
}
