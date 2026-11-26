package net.sigmabeta.sage.ui

/**
 * The accessibility labels the shared renderer suite ([net.sigmabeta.sage.ui.composables])
 * resolves through [StringProvider].
 *
 * An app's own [SageStringId]s live in its own enum; these are the ids SAGE's renderers emit
 * themselves, so every consuming app must map them in its [StringProvider]. They cover the
 * "on-click label" and "state description" strings for the checkbox, dropdown, rating, value and
 * single-line rows.
 */
enum class SageComponentStringId : SageStringId {
    /** "Toggle checkbox" — a checkbox row's click action. */
    ACCY_OCL_CHECKBOX,

    /** "Expand dropdown" — a dropdown row's click action. */
    ACCY_OCL_DROPDOWN,

    /** "Rate" — a rating row's click action. */
    ACCY_OCL_RATING,

    /** "Open %1$s" — a single-line row's click action, naming its value. */
    ACCY_OCL_SINGLE_LINE,

    /** "Open" — a value row's click action. */
    ACCY_OCL_VALUE,

    /** "Checked" — the checked state of a checkbox row. */
    ACCY_ST_DESC_CHECKED,

    /** "Loading" — a checkbox row whose state is still loading. */
    ACCY_ST_DESC_LOADING,

    /** "Unchecked" — the unchecked state of a checkbox row. */
    ACCY_ST_DESC_UNCHECKED,
}
