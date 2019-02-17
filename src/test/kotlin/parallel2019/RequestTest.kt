package parallel2019

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

object RequestTest {

    @Test
    fun parseSuccessful() {
        val req = Request.parse("GET /123/someURI/somePart/ protocol")
        assertTrue(req is Request.Delay)
        req as Request.Delay
        assertEquals("GET", req.verb)
        assertEquals(123L, req.delay)
        assertEquals("someURI/somePart/", req.uri)
    }

    @Test
    fun parseError() {
        val req = Request.parse("someError")
        assertTrue(req is Request.Error)
    }

    @Test
    fun `handle errors that have 3 parts`() {
        val req = Request.parse("someError in threeParts")
        assertTrue(req is Request.Error)
    }

    @Test
    fun `createRequestFromUri should split a valid HTTP line`() {
        val delayInMs = "1234"
        val origUri = "https://somehost:9876/path/to/file.html"
        val request = Request.createRequestFromUri("GET", "/$delayInMs/$origUri")
        assertTrue(request is Request.Delay)
        request as Request.Delay
        assertEquals("GET", request.verb)
        assertEquals(delayInMs.toLong(), request.delay)
        assertEquals(origUri, request.uri)
    }

    @Test
    fun `splitRequestLine should split a valid HTTP line`() {
        val lines = Request.splitRequestLine("GET /index.html HTTP/1.1")
        assertEquals(3, lines.size)
        assertEquals("GET", lines[0])
        assertEquals("/index.html", lines[1])
        assertEquals("HTTP/1.1", lines[2])
    }
}