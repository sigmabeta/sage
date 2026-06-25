# `:common:connectivity`

> Network-status types and request exceptions — SAGE's view of whether the device (and the API) is reachable.

The shared contract for observing connectivity: a `NetworkStatus` enum, a
`NetworkStatusProvider` that exposes it as a `StateFlow` and can probe the API,
and two `IOException` subtypes thrown when a request can't proceed. It's an
`:api`-style module — the provider implementation lives elsewhere and is wired
via DI. The exceptions touch `java.io.IOException`, so they live in the
`jvmSharedMain` source set (`src/main/java`) and are Android+JVM only; the status
types are pure `commonMain`.

## Contents

| File | What it is |
| --- | --- |
| `NetworkStatus.kt` | `enum NetworkStatus`: `OFFLINE`, `ONLINE_NO_INTERNET`, `ONLINE`, `ONLINE_API_UNREACHABLE`, plus the `NetworkStatus.allowsApiRequests` extension (true only for `ONLINE`). |
| `NetworkStatusProvider.kt` | `interface NetworkStatusProvider`: a `StateFlow<NetworkStatus> status` and `suspend fun checkApiAvailability()`. |
| `HttpException.kt` | `IOException` with an HTTP `code` — a non-2xx response. (`jvmSharedMain`) |
| `NetworkUnavailableException.kt` | `IOException` carrying the `NetworkStatus` that blocked the request. (`jvmSharedMain`) |

## Why depend on this module

Depend on `:common:connectivity` when code must gate work on connectivity —
deciding whether to make an API call, surfacing offline state in the UI, or
classifying request failures. Depend on this `:api` module for the types; the
app wires a concrete `NetworkStatusProvider` via DI.

## Using it

```kotlin
class Fetcher(private val network: NetworkStatusProvider) {
    suspend fun fetch(): Result {
        val status = network.status.value
        if (!status.allowsApiRequests) {
            throw NetworkUnavailableException(status, "no usable network")
        }
        // ... perform request; on non-2xx throw HttpException(code, body)
    }
}
```

## Module facts

- **Plugin:** `sage.kmp`
- **Targets:** Android + JVM
- **Source set:** `commonMain` (status types) + `src/main/java` (`jvmSharedMain`, the `java.io.IOException` subtypes)
- **SAGE/module dependencies:** `:common:coroutines`
