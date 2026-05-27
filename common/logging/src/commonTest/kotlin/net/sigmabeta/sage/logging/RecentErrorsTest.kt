package net.sigmabeta.sage.logging

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class RecentErrorsTest {
    @Test
    fun `BluntHatchet e() pushes message into recentErrors`() {
        val hatchet = BluntHatchet()
        hatchet.e("boom")
        assertEquals(listOf("boom"), hatchet.recentErrors.map { it.message })
    }

    @Test
    fun `BluntHatchet log() at severity ERROR pushes into recentErrors`() {
        val hatchet = BluntHatchet()
        hatchet.log(SEVERITY_ERROR, "boom-via-log")
        assertEquals(listOf("boom-via-log"), hatchet.recentErrors.map { it.message })
    }

    @Test
    fun `BluntHatchet log() below ERROR does not push`() {
        val hatchet = BluntHatchet()
        hatchet.log(SEVERITY_WARN, "warn")
        hatchet.log(SEVERITY_INFO, "info")
        hatchet.log(SEVERITY_DEBUG, "debug")
        hatchet.log(SEVERITY_VERBOSE, "verbose")
        assertTrue(hatchet.recentErrors.isEmpty())
    }

    @Test
    fun `BluntHatchet log() at ASSERT (above ERROR) still pushes`() {
        val hatchet = BluntHatchet()
        hatchet.log(SEVERITY_ASSERT, "wtf")
        assertEquals(listOf("wtf"), hatchet.recentErrors.map { it.message })
    }

    @Test
    fun `BluntHatchet queue caps at 16 with FIFO eviction`() {
        val hatchet = BluntHatchet()
        repeat(20) { hatchet.e("err-$it") }
        val messages = hatchet.recentErrors.map { it.message }
        assertEquals(16, messages.size)
        // Oldest entries (err-0..err-3) were evicted; err-4..err-19 remain, oldest first.
        assertEquals("err-4", messages.first())
        assertEquals("err-19", messages.last())
    }

    @Test
    fun `BasicHatchet behaves the same as BluntHatchet for the queue`() {
        val hatchet = BasicHatchet()
        hatchet.e("one")
        hatchet.log(SEVERITY_ERROR, "two")
        hatchet.w("ignored")
        assertEquals(listOf("one", "two"), hatchet.recentErrors.map { it.message })
    }

    @Test
    fun `recentErrors snapshot is independent of subsequent writes`() {
        val hatchet = BluntHatchet()
        hatchet.e("first")
        val snapshot = hatchet.recentErrors
        hatchet.e("second")
        // Snapshot taken before the second push must not see the new entry.
        assertEquals(listOf("first"), snapshot.map { it.message })
        assertEquals(listOf("first", "second"), hatchet.recentErrors.map { it.message })
    }

    @Test
    fun `entry timestamp is populated`() {
        val hatchet = BluntHatchet()
        hatchet.e("ts-check")
        assertTrue(hatchet.recentErrors.single().timestamp > 0)
    }

    companion object {
        // Mirrors android.util.Log severity ints to avoid pulling in the Android dep just for these.
        private const val SEVERITY_VERBOSE = 2
        private const val SEVERITY_DEBUG = 3
        private const val SEVERITY_INFO = 4
        private const val SEVERITY_WARN = 5
        private const val SEVERITY_ERROR = 6
        private const val SEVERITY_ASSERT = 7
    }
}
