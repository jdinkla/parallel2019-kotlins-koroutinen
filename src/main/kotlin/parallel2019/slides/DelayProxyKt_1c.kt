package parallel2019.slides.dp1c

import parallel2019.Request.Delay
import parallel2019.Utils
import parallel2019.Utils.withSocket
import java.net.ServerSocket
import java.net.Socket
import java.net.http.HttpResponse

private val bodyHandler = HttpResponse.BodyHandlers.ofString()!!

class RequestHandler(private val socket: Socket) : Thread() {
    override fun run() {
        withSocket(socket) { request -> delayAndGet(request) }
    }
}

fun delayAndGet(request: Delay): String {
    Thread.sleep(request.delay)
    val httpRequest = Utils.httpRequest(request.uri)
    val response = Utils.client.send(httpRequest, bodyHandler)
    return Utils.createHttpResponse(response)
}

fun main(args: Array<String>) {
    ServerSocket(Utils.PORT).use { serverSocket ->
        while (true) {
            val socket = serverSocket.accept()
            RequestHandler(socket).start()
        }
    }
}

