# `:android:coroutines`

> Android dispatcher wiring — Metro bindings for `SageDispatchers` and the app `CoroutineScope`.

The Android-only DI provider for `common:coroutines`. It binds the platform
`SageDispatchers` (mapping computation/disk/network/main onto `kotlinx.coroutines`
`Dispatchers`) and an app-scoped `CoroutineScope` into the Metro graph.

## Contents

| File | What it is |
| --- | --- |
| `CoroutinesModule.kt` | A `@BindingContainer @ContributesTo(AppScope::class)` Metro object. Provides `SageDispatchers(computation = Default, disk = IO, network = IO, main = Main)` and an app-scoped `CoroutineScope` running on the computation dispatcher, both `@SingleIn(AppScope::class)`. |

## Why depend on this module

This module supplies the Android-side DI bindings for the `common:coroutines`
API. Depend on `:common:coroutines` for the `SageDispatchers` type; include
`:android:coroutines` in the Android app so the Metro `AppScope` graph can inject
a real `SageDispatchers` and `CoroutineScope`. The `metro` plugin with
`interop.includeDagger()` lets the Dagger-shaped `@Provides` annotations
contribute into Metro.

## Using it

No call-style API — it is pure DI. `@ContributesTo(AppScope::class)` flows its
bindings into whatever Metro graph declares `AppScope`, after which consumers
inject them:

```kotlin
@Inject
class Player(
    private val dispatchers: SageDispatchers,
    private val scope: CoroutineScope,
)
```

## Module facts

- **Plugin:** `sage.android` + `metro` (`interop.includeDagger()`)
- **Targets:** Android only
- **Source set:** `src/main/java`
- **SAGE/module dependencies:** `api(projects.common.coroutines)`, `implementation(projects.common.di)`
