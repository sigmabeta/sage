# `:common:di`

> The `AppScope` marker that names SAGE's application-wide DI scope.

A single pure-Kotlin marker class. DI in this project is **Metro**
(`dev.zacsweers.metro`), and `AppScope` is its equivalent of Hilt's
`SingletonComponent` — the compile-time tag every app-lifetime binding is scoped
to.

## Contents

| File | What it is |
| --- | --- |
| `AppScope.kt` | `class AppScope private constructor()` — never instantiated; Metro uses the `KClass` reference as a tag. |

## Why depend on this module

Depend on `:common:di` from any module that contributes bindings to the
application graph. Hosting the marker in SAGE (rather than in the app) means a
SAGE module **and** a downstream chipbox module can both contribute to the same
scope without creating a `sage → app` dependency cycle. The module carries no
Metro runtime dependency itself — consumers apply the Metro plugin and reference
`AppScope` by fully-qualified name in their annotations.

## Using it

```kotlin
// On the app's graph — it owns AppScope-scoped instances:
@DependencyGraph(AppScope::class)
interface AppGraph

// Mark a binding as an app-wide singleton:
@SingleIn(AppScope::class)
@Inject
class Cache(/* … */)

// Contribute a binding / module into the AppScope graph from any module:
@ContributesBinding(AppScope::class)
@Inject
class RealThing(/* … */) : Thing
```

See `arch-docs/architecture/sage-integration.md` (chipbox) for the full Metro wiring
story.

## Module facts

- **Plugin:** `sage.kmp` + `sage.kmp.js`
- **Targets:** Android + JVM; JS (Node) when built with `-Pchipbox.js`
- **Source set:** `commonMain`
- **SAGE dependencies:** none (leaf)
