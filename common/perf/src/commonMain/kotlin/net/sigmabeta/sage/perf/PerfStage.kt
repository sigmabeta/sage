package net.sigmabeta.sage.perf

enum class PerfStage {
    VIEW_CREATED,
    TITLE_LOADED,
    TRANSITION_START,
    PARTIAL_CONTENT_LOAD,
    FULL_CONTENT_LOAD,
    CANCELLATION,
    COMPLETION
}
