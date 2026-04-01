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

    @Test fun `history records transitions`() {
        val sm = buildMachine()
        sm.send(OrderEvent.Confirm)
        sm.send(OrderEvent.Ship)
        val history = sm.history()
        assertEquals(2, history.size)
        assertEquals(OrderState.Pending, history[0].from)
        assertEquals(OrderState.Confirmed, history[0].to)
        assertEquals(OrderState.Confirmed, history[1].from)
        assertEquals(OrderState.Shipped, history[1].to)
    }

    @Test fun `history is empty initially`() {
        val sm = buildMachine()
        assertTrue(sm.history().isEmpty())
    }

    @Test fun `availableEvents returns valid events for current state`() {
        val sm = buildMachine()
        val events = sm.availableEvents()
        assertEquals(2, events.size)
        assertTrue(events.contains(OrderEvent.Confirm::class))
        assertTrue(events.contains(OrderEvent.Cancel::class))
    }

    @Test fun `availableEvents changes after transition`() {
        val sm = buildMachine()
        sm.send(OrderEvent.Confirm)
        val events = sm.availableEvents()
        assertEquals(1, events.size)
        assertTrue(events.contains(OrderEvent.Ship::class))
    }

    @Test fun `availableEvents is empty for terminal state`() {
        val sm = buildMachine()
        sm.send(OrderEvent.Confirm)
        sm.send(OrderEvent.Ship)
        assertTrue(sm.availableEvents().isEmpty())
    }

    @Test fun `reset returns to initial state`() {
        val sm = buildMachine()
        sm.send(OrderEvent.Confirm)
        sm.send(OrderEvent.Ship)
        assertEquals(OrderState.Shipped, sm.currentState)
        sm.reset()
        assertEquals(OrderState.Pending, sm.currentState)
        assertTrue(sm.history().isEmpty())
    }
}
