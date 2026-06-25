# `:common:settings:environment`

> Persists and streams the app's selected backend environment.

A tiny settings slice for choosing among a fixed list of `AppEnvironment`s (e.g.
prod vs. staging endpoints). It stores the chosen environment's index in
`:common:storage:common`'s `Storage` and exposes the current selection as a `Flow`,
falling back to a supplied default when nothing (or something out of range) is
stored.

## Contents

| File | What it is |
| --- | --- |
| `AppEnvironment.kt` | Interface for an environment; a single nullable `url: String?`. Implementations are provided by the app. |
| `EnvironmentManager.kt` | Holds the `environments` list + a `default`. `setEnvironment(env)` saves its list index; `selectedEnvironmentFlow()` maps the stored index back to an `AppEnvironment`, returning `default` when null or out of bounds. |

## Why depend on this module

Depend on `:common:settings:environment` where you need to read or change which
backend environment is active. Provide your own `AppEnvironment` implementations and
construct an `EnvironmentManager` with the full list plus the default; a `Storage`
must be wired below it.

## Using it

```kotlin
val manager = EnvironmentManager(
    storage = storage,
    environments = listOf(prod, staging),
    default = prod,
)

manager.selectedEnvironmentFlow().collect { env -> useBaseUrl(env.url) }
manager.setEnvironment(staging)
```

## Module facts

- **Plugin:** `sage.kmp`
- **Targets:** Android + JVM
- **Source set:** `commonMain` (deps declared on the `jvmSharedMain` source set)
- **SAGE/module dependencies:** `:common:coroutines`, `:common:storage:common`
