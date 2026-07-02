# `:common:nav`

> The platform-agnostic vocabulary for describing a navigable route.

Defines what a "route" is in the abstract — a name plus an argument shape — so
that feature `:api` modules can declare destinations without depending on any
particular navigation library. A leaf module.

## Contents

| File | What it is |
| --- | --- |
| `RouteDescriptor.kt` | `interface RouteDescriptor { val destName; val argType }` — the minimal description of a destination. |
| `ArgType.kt` | `enum ArgType { NONE, LONG, STRING, TWO }` describing a route's argument arity/type, plus `ARG_TEMPLATE_ONE` / `ARG_TEMPLATE_TWO` argument-key constants. |

## Why depend on this module

Depend on `:common:nav` when defining or consuming route metadata. It exists so
the description of a route is decoupled from the navigation engine. In chipbox
the actual navigation is **Voyager**, and route keys are `@Serializable` objects
in each feature's `:api`; this module supplies the shared, library-neutral
notion those build on.

## Using it

```kotlin
enum class MyRoutes(
    override val destName: String,
    override val argType: ArgType,
) : RouteDescriptor {
    List("my_list", ArgType.NONE),
    Detail("my_detail", ArgType.LONG),
}
```

## Module facts

- **Plugin:** `sage.kmp` + `sage.kmp.js`
- **Targets:** Android + JVM; JS (Node) when built with `-Psage.js`
- **Source set:** `commonMain`
- **SAGE dependencies:** none (leaf)
