package parallel2019.ktor

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class DelayedKtorTest {

    @Test
    fun `tail should handle empty lists`() {
        assertEquals(listOf<String>(), listOf<String>().tail())
    }

    @Test
    fun `tail should handle list with one element`() {
        assertEquals(listOf<String>(), listOf("a").tail())
    }

    @Test
    fun `tail should handle list with many elements`() {
        assertEquals(listOf("b"), listOf("a", "b").tail())
    }

    @Test
    fun `joinUrlParts should insert correct number of slashes`() {
        val parts = listOf("http:", "www.dinkla.net", "en", "index.html")
        assertEquals("http://www.dinkla.net/en/index.html", parts.joinWithSlashes())
    }

    @Test
    fun `joinUrlParts should handle empty list`() {
        assertEquals("", listOf<String>().joinWithSlashes())
    }

    @Test
    fun `joinUrlParts should handle list with one element only`() {
        assertEquals("http://", listOf("http:").joinWithSlashes())
    }

    @Test
    fun `joinUrlParts should handle list with hostname only`() {
        assertEquals("http://www.dinkla.net", listOf("http:", "www.dinkla.net").joinWithSlashes())
    }

}