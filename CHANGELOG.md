# Changelog

## 0.2.0 (2026-03-31)

- Add `history()` to get a chronological list of all transitions made
- Add `availableEvents()` to get the set of valid events for the current state
- Add `reset()` to return to the initial state and clear history
- Add `Transition<S, E>` data class recording from, event, to, and timestamp

## 0.1.6 (2026-03-31)

- Standardize README to 3-badge format with emoji Support section
- Update CI checkout action to v5 for Node.js 24 compatibility
- Add GitHub issue templates, dependabot config, and PR template

## 0.1.5 (2026-03-22)

- Fix README compliance (badge label, installation format), standardize CHANGELOG

## 0.1.4 (2026-03-20)

- Standardize README: fix title, badges, version sync, remove Requirements section

## 0.1.3 (2026-03-18)

- Add explicit types to @PublishedApi internal properties for explicitApi compliance

## 0.1.2 (2026-03-18)

- Fix PublishedApi visibility for inline reified functions in StateMachineBuilder

## 0.1.1 (2026-03-18)

- Upgrade to Kotlin 2.0.21 and Gradle 8.12
- Enable explicitApi() for stricter public API surface
- Add issueManagement to POM metadata

## 0.1.0 (2026-03-18)

- `stateMachine()` DSL for defining states and transitions
- Type-safe states and events
- Transition guards and side effects
- `canSend()` to check transition validity
- Global transition listener
