# `:common:analytics`

> The `Analytics` event-logging interface — screen views, actions, errors — plus a logging no-op default.

SAGE's analytics seam: an `Analytics` interface for recording screen views,
actions, generic events, auto-refreshes, and errors, expressed in terms of the
`:common:appcomm` action/event vocabulary. It ships `NoopAnalytics`, which logs
everything through `Hatchet` instead of sending it anywhere — the safe default
when no real backend is wired. Concrete reporters live elsewhere and are wired
via DI.

## Contents

| File | What it is |
| --- | --- |
| `Analytics.kt` | `interface Analytics`: `logScreenView`, `logAction`, `logEvent`, `logAutoRefresh`, `logError`. |
| `AnalyticsScreenId.kt` | Marker `interface AnalyticsScreenId` — the opaque screen-identity type log calls take. |
| `AnalyticsScreen.kt` | `enum AnalyticsScreen : AnalyticsScreenId` — SAGE's built-in screens (`HUD`, `SEARCH`, `SETTINGS`, `LICENSE`, `ABOUT`, `DEBUG`, `DEEPLINK`, `NONE`). |
| `NoopAnalytics.kt` | `open class NoopAnalytics(Hatchet)` — logs each call via `Hatchet` and reports nothing externally. |
| `ActionUtils.kt` | `SageAction.isInitAction()` and `SageAction.getDetails()` extensions — classify init actions and extract their argument string for logging. |

## Why depend on this module

Depend on `:common:analytics` when a reducer/ViewModel needs to record what the
user did or saw. Inject the `Analytics` interface; the app supplies a real
reporter or falls back to `NoopAnalytics`. The module `api`-exposes
`:common:appcomm`, so its action/event types come along for free.

## Using it

```kotlin
class MyReducer(private val analytics: Analytics) {
    fun onEnter(action: SageAction) {
        analytics.logScreenView(action, AnalyticsScreen.SETTINGS)
        if (action.isInitAction()) {
            // action.getDetails() yields e.g. the id or query that opened the screen
        }
    }
}

// Default wiring when no backend is configured:
val analytics: Analytics = NoopAnalytics(hatchet)
```

## Module facts

- **Plugin:** `sage.kmp` + `sage.kmp.js`
- **Targets:** Android + JVM; JS (Node) when built with `-Pchipbox.js`
- **Source set:** `commonMain`
- **SAGE/module dependencies:** `:common:appcomm` (`api`), `:common:logging`
