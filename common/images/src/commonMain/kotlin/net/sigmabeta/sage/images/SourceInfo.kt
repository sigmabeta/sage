package net.sigmabeta.sage.images

data class SourceInfo(
    val info: Any?
) {
    companion object {
        /**
         * The source for an item whose image is made from zero or more URLs: no image when [urls] is
         * empty, the image itself when there's one, and an [ImageCollage] of the first
         * [ImageCollage.MAX_SOURCES] when there are more.
         */
        fun ofUrls(urls: List<String>): SourceInfo = when (urls.size) {
            0 -> SourceInfo(null)
            1 -> SourceInfo(urls.single())
            else -> SourceInfo(ImageCollage(urls.take(ImageCollage.MAX_SOURCES)))
        }
    }
}
