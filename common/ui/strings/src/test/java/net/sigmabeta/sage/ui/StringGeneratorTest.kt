package net.sigmabeta.sage.ui

import java.util.Random
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class StringGeneratorTest {
    @Test
    fun `random words never contain embedded whitespace`() {
        StringGenerator.RANDOM_WORDS.forEach { word ->
            assertFalse(
                word.any(Char::isWhitespace),
                "Word contains embedded whitespace: '$word'",
            )
        }
    }

    @Test
    fun `generated lorem never contains embedded whitespace`() {
        val generator = StringGenerator(Random(42))

        repeat(100) {
            generator.generateLorem().split(' ').forEach { word ->
                assertFalse(
                    word.any(Char::isWhitespace),
                    "Generated word contains embedded whitespace: '$word'",
                )
            }
        }
    }

    @Test
    fun `generated titles never contain embedded whitespace`() {
        val generator = StringGenerator(Random(42))

        repeat(100) {
            generator.generateTitle().split(' ').forEach { word ->
                assertFalse(
                    word.any(Char::isWhitespace),
                    "Generated word contains embedded whitespace: '$word'",
                )
            }
        }
    }

    @Test
    fun `generated names are two non-blank words`() {
        val generator = StringGenerator(Random(42))

        repeat(100) {
            val words = generator.generateName().split(' ')
            assertTrue(words.size == 2 && words.all { it.isNotBlank() })
        }
    }
}
