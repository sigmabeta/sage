# `:android:analytics`

> Firebase Analytics backend — an `Analytics` impl plus the Metro binding for `FirebaseAnalytics`.

The Android-only implementation of `common:analytics`'s `Analytics` interface,
backed by Firebase Analytics. It logs screen views, actions, app events, and
errors as Firebase events, and provides the `FirebaseAnalytics` instance via
Metro.

## Contents

| File | What it is |
| --- | --- |
| `firebase/FirebaseAnalyticsImpl.kt` | `Analytics` impl. `logScreenView`/`logAction`/`logEvent`/`logAutoRefresh`/`logError` build a `Bundle` and fire a named Firebase event on `dispatchers.network` via the injected `CoroutineScope`. Event/param names and the 128-char error-trace cap are companion constants. |
| `AnalyticsModule.kt` | `@BindingContainer @ContributesTo(AppScope::class)` Metro object providing an app-scoped `FirebaseAnalytics` from `FirebaseAnalytics.getInstance(context)` (with a documented `@SuppressLint("MissingPermission")`). |

## Why depend on this module

Depend on `:common:analytics` for the `Analytics` type and SAGE action/event
types; include `:android:analytics` in the Android app to log to Firebase. The
`metro` plugin with `interop.includeDagger()` makes `AnalyticsModule`'s
Dagger-shaped `@Provides` contribute the `FirebaseAnalytics` binding into the
app's Metro `AppScope` graph. Requires the Firebase BoM/`firebase-analytics`
dependency, supplied here.

## Using it

`FirebaseAnalyticsImpl` is the `Analytics` implementation; construct it with the
DI-provided `FirebaseAnalytics`, `SageDispatchers`, and app `CoroutineScope`:

```kotlin
val analytics: Analytics = FirebaseAnalyticsImpl(firebaseAnalytics, dispatchers, scope)
analytics.logScreenView(action, screenId)
analytics.logError("loadGame", "decode failed", throwable)
```

## Module facts

- **Plugin:** `sage.android` + `metro` (`interop.includeDagger()`)
- **Targets:** Android only
- **Source set:** `src/main/java`
- **SAGE/module dependencies:** `api(projects.common.analytics)`, `implementation(projects.common.di)`, `implementation(projects.android.coroutines)`; Firebase BoM + `firebase-analytics`
