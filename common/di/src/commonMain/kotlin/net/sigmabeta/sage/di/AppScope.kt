package net.sigmabeta.sage.di

/**
 * Marker class identifying the application-wide DI scope — Metro's equivalent of Hilt's
 * `SingletonComponent`. Used in:
 *
 *  - `@DependencyGraph(AppScope::class)` on the app's graph: declares that the graph owns
 *    instances scoped to this marker.
 *  - `@SingleIn(AppScope::class)` on `@Inject` classes or `@Provides` methods: marks the
 *    binding as a singleton within an `AppScope`-scoped graph (replaces `@Singleton`).
 *  - `@ContributesTo(AppScope::class)` on interfaces / objects with `@Provides` / `@Binds`
 *    members: Anvil-style module aggregation (replaces Hilt's
 *    `@InstallIn(SingletonComponent::class)`).
 *  - `@ContributesBinding(AppScope::class)` on `@Inject` classes: binds the class to its
 *    declared interface within the scope (replaces `@Module abstract class { @Binds }`).
 *
 * Private constructor: this class is never instantiated — Metro uses the `KClass` reference
 * as a compile-time tag. Hosted in sage so any module — sage's own or downstream consumers'
 * — can contribute to the same scope without forcing a sage → app dependency cycle.
 */
public class AppScope private constructor()
