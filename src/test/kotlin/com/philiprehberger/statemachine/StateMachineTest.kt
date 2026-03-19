package com.philiprehberger.statemachine

import kotlin.test.*

sealed class OrderState { object Pending : OrderState(); object Confirmed : OrderState(); object Shipped : OrderState(); object Cancelled : OrderState() }
sealed class OrderEvent { object Confirm : OrderEvent(); object Ship : OrderEvent(); object Cancel : OrderEvent() }

class StateMachineTest {
    private fun buildMachine() = stateMachine<OrderState, OrderEvent>(OrderState.Pending) {
        state<OrderState.Pending> {
            on<OrderEvent.Confirm> { transitionTo(OrderState.Confirmed) }
            on<OrderEvent.Cancel> { transitionTo(OrderState.Cancelled) }
        }
        state<OrderState.Confirmed> {
            on<OrderEvent.Ship> { transitionTo(OrderState.Shipped) }
        }
    }

    @Test fun `basic transitions`() {
        val sm = buildMachine()
        assertEquals(OrderState.Pending, sm.currentState)
        sm.send(OrderEvent.Confirm)
        assertEquals(OrderState.Confirmed, sm.currentState)
        sm.send(OrderEvent.Ship)
        assertEquals(OrderState.Shipped, sm.currentState)
    }
    @Test fun `invalid transition throws`() {
        val sm = buildMachine()
        assertFailsWith<IllegalStateException> { sm.send(OrderEvent.Ship) }
    }
    @Test fun `canSend`() {
        val sm = buildMachine()
        assertTrue(sm.canSend(OrderEvent.Confirm))
        assertFalse(sm.canSend(OrderEvent.Ship))
    }
}
