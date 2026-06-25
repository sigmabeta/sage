# `:common:events`

> The real `EventDispatcher` — fans `SageEvent`s out to analytics and registered sinks.

The concrete implementation of `EventDispatcher` (declared in `:common:appcomm`).
Every `SageEvent` it receives is logged to `Analytics` and forwarded to any
`EventSink`s that have registered. This is the `:real` half of the events seam: the
interface lives in `:common:appcomm`, the wiring is supplied here.

## Contents

| File | What it is |
| --- | --- |
| `EventDispatcherReal.kt` | `EventDispatcher` impl. Holds a mutable set of `EventSink`s; `sendEvent` logs the event via `Analytics` then forwards it to each sink. `addEventSink`/`removeEventSink` manage the set. |

## Why depend on this module

Depend on `:common:events` only where the app graph is wired — it's the binding
that satisfies the `EventDispatcher` interface. Everything else should depend on
`:common:appcomm` for the `EventDispatcher`/`EventSink`/`SageEvent` types and stay
ignorant of this implementation. `EventDispatcherReal` requires an `Analytics`
(from `:common:analytics`), so it sits below the analytics layer in the graph.

## Using it

```kotlin
val dispatcher: EventDispatcher = EventDispatcherReal(analytics)

// Components that want to observe events register a sink:
dispatcher.addEventSink(mySink)

// Anything can publish; analytics + every sink receive it:
dispatcher.sendEvent(SomeSageEvent)
```

## Module facts

- **Plugin:** `sage.kmp`
- **Targets:** Android + JVM
- **Source set:** `commonMain` (deps declared on the `jvmSharedMain` source set)
- **SAGE/module dependencies:** `:common:analytics`, `:common:appcomm`
