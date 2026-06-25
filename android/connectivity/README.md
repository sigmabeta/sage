# `:android:connectivity`

> Android network-status provider — `ConnectivityManager`-backed `NetworkStatusProvider`.

The Android-only implementation of `common:connectivity`'s
`NetworkStatusProvider`. It observes the system `ConnectivityManager` and exposes
a `StateFlow<NetworkStatus>`, optionally confirming reachability of a backend API
via a caller-supplied probe.

## Contents

| File | What it is |
| --- | --- |
| `AndroidNetworkStatusProvider.kt` | `NetworkStatusProvider` impl. Registers a default/`NET_CAPABILITY_INTERNET` `NetworkCallback`, seeds initial state from `activeNetwork`, and maps `NetworkCapabilities` to `NetworkStatus` (OFFLINE / ONLINE_NO_INTERNET / ONLINE / ONLINE_API_UNREACHABLE). When ONLINE it runs the supplied `apiProbe` (mutex-guarded, on `dispatchers.network`) to distinguish reachable from unreachable; `checkApiAvailability()` re-probes on demand. Handles API-level differences (M/N). |

## Why depend on this module

Depend on `:common:connectivity` for the `NetworkStatus`/`NetworkStatusProvider`
types; include `:android:connectivity` in the Android app to get the
`ConnectivityManager`-backed implementation. There are no DI bindings here — the
app constructs `AndroidNetworkStatusProvider` with a `Context`, a `Hatchet`,
`SageDispatchers`, and the API-probe lambda, then binds it as the
`NetworkStatusProvider`.

## Using it

```kotlin
val provider: NetworkStatusProvider = AndroidNetworkStatusProvider(
    context = appContext,
    hatchet = hatchet,
    dispatchers = dispatchers,
    apiProbe = { httpClient.ping() }, // suspend () -> Boolean
)

provider.status.collect { status -> /* react to NetworkStatus */ }
provider.checkApiAvailability() // force a re-probe
```

## Module facts

- **Plugin:** `sage.android`
- **Targets:** Android only
- **Source set:** `src/main/java`
- **SAGE/module dependencies:** `api(projects.common.connectivity)`, `implementation(projects.common.coroutines)`, `implementation(projects.common.logging)`
