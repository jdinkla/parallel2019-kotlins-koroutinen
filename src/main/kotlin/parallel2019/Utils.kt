package parallel2019

import parallel2019.Request.Delay
import parallel2019.Request.Error
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpHeaders
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Duration
import java.time.Instant
import java.util.*
import java.util.stream.Collectors

object Utils {

    const val PORT = 9090

    const val RC_501 = "HTTP/1.1 501 Not Implemented"

    fun print(msg: String) {
        println("[" + Thread.currentThread().name + "] " + msg)
    }

    fun newReader(socket: Socket): BufferedReader = BufferedReader(InputStreamReader(socket.getInputStream()))

    fun newWriter(socket: Socket): PrintWriter = PrintWriter(socket.getOutputStream(), true)

    private fun getHeader(inp: BufferedReader): List<String> {
        val header = LinkedList<String>()
        var line: String = inp.readLine()
        while (line != "") {
            header.add(line)
            line = inp.readLine()
        }
        return header
    }

    fun parseRequest(inp: BufferedReader): Request {
        return try {
            Request.parse(getHeader(inp)[0])
        } catch (e: IndexOutOfBoundsException) {
            Error
        }
    }

    val client: HttpClient by lazy {
        HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build()
    }

    fun get(uri: String): HttpResponse<String> {
        val request = HttpRequest.newBuilder()
            .uri(URI.create(uri))
            .GET()
            .build()
        return client.send(request, HttpResponse.BodyHandlers.ofString())
    }

    fun createHttpResponse(response: HttpResponse<String>): String {
        val headers = response.headers().asString()
        val body = response.body().toString()
        return "HTTP/1.1 200 Ok\n$headers\n\n$body\n"
    }

    fun HttpHeaders.asString(): String {
        val collect = this
            .map()
            .entries.stream()
            .map { e -> e.key + ": " + e.value[0] }
            .collect(Collectors.toList())
        return collect.joinToString("\n")
    }

    fun httpRequest(uri: String) = HttpRequest.newBuilder()
        .uri(URI.create(uri))
        .GET()
        .build()!!

    fun withSocket(socket: Socket, block: (BufferedReader, PrintWriter) -> Unit) {
        val theReader = BufferedReader(InputStreamReader(socket.getInputStream()))
        val theWriter = PrintWriter(socket.getOutputStream(), true)
        theReader.use { reader ->
            theWriter.use { writer ->
                block(reader, writer)
            }
        }
    }

    suspend fun withSocketSuspend(socket: Socket, block: suspend (BufferedReader, PrintWriter) -> Unit) {
        val theReader = BufferedReader(InputStreamReader(socket.getInputStream()))
        val theWriter = PrintWriter(socket.getOutputStream(), true)
        theReader.use { reader ->
            theWriter.use { writer ->
                block(reader, writer)
            }
        }
    }

    fun withSocket(socket: Socket, block: (Delay) -> String) {
        withSocket(socket) { reader, writer ->
            val request = Utils.parseRequest(reader)
            val response = when (request) {
                is Delay -> block(request)
                is Error -> Utils.RC_501
            }
            writer.println(response)
        }
    }

    suspend fun withSocketSuspend(socket: Socket, block: suspend (Delay) -> String) {
        withSocketSuspend(socket) { reader, writer ->
            val request = Utils.parseRequest(reader)
            val response = when (request) {
                is Delay -> block(request)
                is Error -> Utils.RC_501
            }
            writer.println(response)
        }
    }

    fun debug(s: String) {
        println("$s [${Thread.currentThread().name}] [${Instant.now()}]")
    }
}
