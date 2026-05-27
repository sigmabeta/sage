package net.sigmabeta.sage.components

/**
 * Pixel-size buckets used when asking Coil for a bitmap for a constraint-sized image cell.
 *
 * The cell layout is unaffected (cells still measure to whatever the surrounding column /
 * grid hands them); only the size we send to Coil is snapped to one of these buckets so
 * that two cells with slightly different measured pixels share a memory-cache entry rather
 * than each pinning their own decoded bitmap.
 *
 * [pixels] is the max-dimension target — when a cell measures with max(width, height) ≤
 * this value, the request goes out at [pixels] × [pixels].
 */
enum class GridImageSize(val pixels: Int) {
    Small(192),
    Medium(384),
    Large(512),
    XLarge(784),
}
