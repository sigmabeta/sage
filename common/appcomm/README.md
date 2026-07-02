# `:common:appcomm`

> SAGE's app-wide communication vocabulary — actions, events, and the LCE/state contracts that wire reducers to UI.

The shared type layer for SAGE's unidirectional data flow: the `SageAction`
inputs a screen sends to its reducer, the `SageEvent` outputs the reducer pushes
back to the host (navigation, snackbars, top-bar updates, etc.), the `LCE`
load/content/error envelope, and the sink/dispatcher interfaces that move them
around. It's a leaf-ish `:api`-style module — pure types and `fun interface`s,
no implementations — so any module that participates in the action/event loop
can depend on it without pulling in UI or platform code.

## Contents

| File | What it is |
| --- | --- |
| `SageAction.kt` | `open class SageAction` hierarchy: the inbound user/system intents a reducer handles (`Init*`, `Resume`/`Pause`, back, search, pagination `LoadMore/PreviousRequested`, `Reorder`, inline edit/confirm rows, snackbar/notif callbacks). |
| `SageEvent.kt` | `open class SageEvent` hierarchy: the outbound effects a reducer emits — `NavigateTo`/`NavigateBack`, `ShowSnackbar` (+ `SnackbarActionDetails`), `UpdateTitle`, UI-chrome/system-bars toggles, `ClearNotif`, screen-on timer. Carries a `source` string. |
| `SageState.kt` | Marker `interface SageState` implemented by per-screen state types. |
| `LCE.kt` | `sealed class LCE<out T>`: `Uninitialized` / `Loading(operationName)` / `Content(data)` / `Error(operationName, error)`. |
| `ActionSink.kt` | `fun interface ActionSink { sendAction(SageAction) }` — where a screen pushes its actions. |
| `EventSink.kt` | `fun interface EventSink { sendEvent(SageEvent) }` — a consumer of events. |
| `EventDispatcher.kt` | `interface EventDispatcher`: a `sendEvent` lambda plus add/remove of `EventSink`s — the fan-out hub. |
| `GenericAction.kt` | `@Serializable data class GenericAction` — a flattened (type + id/string args) action payload for serialization (e.g. deep links, snackbar CTAs). |

## Why depend on this module

Depend on `:common:appcomm` whenever your module produces or consumes SAGE
actions/events — reducers, ViewModels, navigation hosts, snackbar/notification
plumbing, analytics. It is the common currency that lets those pieces talk
without depending on each other. Higher layers (`:common:analytics`, feature
modules) `api`-expose it so their callers inherit the vocabulary.

## Using it

```kotlin
class MyReducer(private val events: EventDispatcher) {
    fun reduce(action: SageAction): LCE<MyState> = when (action) {
        is SageAction.InitWithId -> {
            events.sendEvent(SageEvent.UpdateTitle(title = "Item ${action.id}", source = "MyReducer"))
            LCE.Loading("load-item")
        }
        SageAction.DeviceBack -> {
            events.sendEvent(SageEvent.NavigateBack(source = "MyReducer"))
            LCE.Uninitialized
        }
        else -> LCE.Uninitialized
    }
}
```

## Module facts

- **Plugin:** `sage.kmp` + `sage.kmp.js` + `kotlin.serialization`
- **Targets:** Android + JVM; JS (Node) when built with `-Psage.js`
- **Source set:** `commonMain`
- **SAGE/module dependencies:** `:common:logging`, `kotlinx-serialization-core`
