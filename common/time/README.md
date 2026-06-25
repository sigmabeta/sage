# `:common:time`

> Date/time abstractions backed by ThreeTen-bp.

A thin seam over [ThreeTen-bp](https://www.threeten.org/threetenbp/) so the rest
of the app can format and compare dates through an injectable interface rather
than calling static date APIs directly. A leaf module.

## Contents

| File | What it is |
| --- | --- |
| `ThreeTenTime.kt` | Interface: `now()`, `parse()`, `zoneIdFrom()`, `localDateFromString()`, `longDateTextFromMillis()`, `longDateTimeText()`. The injectable clock/formatter seam. |
| `TimeUtils.kt` | `TimeUtils.calculateAgeOf(instant)` — a `Duration` from `instant` to now. |

`ThreeTenTime` is an interface (no impl ships here) so production code depends on
the abstraction and tests can supply a fixed clock; the `api`-exported
`org.threeten.bp.*` types appear in its signatures.

## Why depend on this module

Depend on `:common:time` when a module needs timezone-aware dates, date parsing,
or "how long ago" math and wants it mockable. Routing all clock access through
`ThreeTenTime` means a test can pin `now()` to a fixed instant instead of
fighting the system clock.

## Using it

```kotlin
class LastScanLabel(private val time: ThreeTenTime) {
    fun render(timestampMs: Long) = time.longDateTextFromMillis(timestampMs)
}

val age: Duration = TimeUtils.calculateAgeOf(lastSeenInstant)
```

## Module facts

- **Plugin:** `sage.kmp` (no `sage.kmp.js` — depends on `java.*`/ThreeTen-bp)
- **Targets:** Android + JVM
- **Source set:** `src/main/java` (`jvmSharedMain`) — uses `org.threeten.bp.*`
- **SAGE dependencies:** none (leaf) — re-exports `threeten`
