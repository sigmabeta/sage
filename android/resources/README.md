# `:android:resources`

> Android `ResourceProvider` — string-resource lookup wrapped behind a small interface, plus its Metro bindings.

An Android-only abstraction over `android.content.res.Resources` for fetching
formatted strings, with Metro DI wiring. It defines its own `ResourceProvider`
interface and a `Resources`-backed implementation.

## Contents

| File | What it is |
| --- | --- |
| `ResourceProvider.kt` | Interface: `getString(id: Int, vararg formatArgs: Any?): String`. |
| `RealResourceProvider.kt` | Internal `ResourceProvider` impl delegating to `Resources.getString`. |
| `di/ResourcesModule.kt` | `@BindingContainer @ContributesTo(AppScope::class)` Metro object providing app-scoped `Resources` (from `Context.resources`) and a `ResourceProvider`. |
| `res/layout/fragment_.xml` | A stray `FrameLayout`/`ProgressBar` layout (no Kotlin references it). |

## Why depend on this module

Include `:android:resources` in the Android app so the Metro `AppScope` graph can
inject a `ResourceProvider` for code that needs to resolve string resources by
ID. The `metro` plugin with `interop.includeDagger()` lets the Dagger-shaped
`@Provides` annotations in `ResourcesModule` contribute into the app's Metro
graph. Note this provides its own `ResourceProvider` type — distinct from the
`SageStringId`-based `StringProvider` in `:android:ui:strings`.

## Using it

DI is wired via `@ContributesTo(AppScope::class)`; consumers inject the provider:

```kotlin
@Inject
class Greeter(private val resources: ResourceProvider) {
    fun greet(name: String) = resources.getString(R.string.greeting, name)
}
```

## Module facts

- **Plugin:** `sage.android` + `metro` (`interop.includeDagger()`)
- **Targets:** Android only
- **Source set:** `src/main/java` (+ `src/main/res`)
- **SAGE/module dependencies:** `implementation(projects.common.di)`, `implementation(projects.android.coroutines)`
