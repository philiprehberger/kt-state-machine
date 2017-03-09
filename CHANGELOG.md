# Changelog
## 0.1.4 (2026-03-20)- Standardize README: fix title, badges, version sync, remove Requirements section

## [0.1.3] - 2026-03-18

- Add explicit types to @PublishedApi internal properties for explicitApi compliance

## [0.1.2] - 2026-03-18

- Fix PublishedApi visibility for inline reified functions in StateMachineBuilder

## [0.1.1] - 2026-03-18

- Upgrade to Kotlin 2.0.21 and Gradle 8.12
- Enable explicitApi() for stricter public API surface
- Add issueManagement to POM metadata

## [0.1.0] - 2026-03-18

### Added

- `stateMachine()` DSL for defining states and transitions

- Type-safe states and events

- Transition guards and side effects

- `canSend()` to check transition validity

- Global transition listener
