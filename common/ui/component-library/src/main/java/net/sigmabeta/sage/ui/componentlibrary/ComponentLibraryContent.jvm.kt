package net.sigmabeta.sage.ui.componentlibrary

import net.sigmabeta.sage.ui.StringGenerator
import java.util.Random

// generateTitle() (1-5 words) backs both titles and captions: the components' caption slot is a
// short secondary line, so generateLorem()'s up-to-50-word output would overflow it.
actual fun componentLibraryContent(seed: Long, count: Int): ComponentLibraryContent {
    val generator = StringGenerator(Random(seed))
    return ComponentLibraryContent(
        names = List(count) { generator.generateName() },
        titles = List(count) { generator.generateTitle() },
        captions = List(count) { generator.generateTitle() },
    )
}
