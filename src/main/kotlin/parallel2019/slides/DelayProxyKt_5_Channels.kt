package parallel2019.slides.dp5

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.future.asDeferred
import parallel2019.Request
import parallel2019.Utils
import java.io.BufferedReader
import java.io.PrintWriter
import java.net.ServerSocket
import java.net.Socket
import java.net.http.HttpResponse

private val bodyHandler = HttpResponse.BodyHandlers.ofString()!!

@UseExperimental(ObsoleteCoroutinesApi::class)
class RequestPipeline {
    class RequestStreams(val reader: BufferedReader, val writer: PrintWriter)
    class ParsedRequest(val io: RequestStreams, val request: Request)
    val streams = Channel<RequestStreams>(100)
    val requests = Channel<ParsedRequest>(100)
    val context = Dispatchers.IO
    val ctx = newSingleThreadContext("io")
    suspend fun start() = coroutineScope {
        launch(context) {
            createPipeLine()
        }
        launch(context) {
            handlePipeLine()
        }
    }
    suspend fun add(socket: Socket)= withContext(ctx) {
        val reader = Utils.newReader(socket)
        val writer = Utils.newWriter(socket)
        streams.send(RequestStreams(reader, writer))
    }
    suspend fun createPipeLine()= coroutineScope {
        for (socket in streams) {
            val request = Utils.parseRequest(socket.reader)
            requests.send(ParsedRequest(socket, request))
        }
    }
    suspend fun handlePipeLine()= coroutineScope {
        for (request in requests) {
            when (request.request) {
                is Request.Delay -> delayAndGet(request.request, request.io)
                is Request.Error -> write(request.io, Utils.RC_501)
            }
        }
    }
    suspend fun delayAndGet(request: Request.Delay, io: RequestStreams) = coroutineScope {
        delay(request.delay)
        val response = Utils.client.sendAsync(Utils.httpRequest(request.uri), bodyHandler).asDeferred()
        write(io, Utils.createHttpResponse(response.await()))
    }
    fun CoroutineScope.write(socket: RequestStreams, msg: String) {
        launch(ctx) {
            socket.writer.println(msg)
        }
    }
}

fun listen() = runBlocking(Dispatchers.Default)  {
    val pipeline = RequestPipeline()
    launch {
        pipeline.start()
    }
    ServerSocket(Utils.PORT).use { serverSocket ->
        while (true) {
            val socket = serverSocket.accept()
            launch {
                pipeline.add(socket)
            }
        }
    }
}


fun main(args: Array<String>) = listen()
