package parallel2019.slides.dp1d

import parallel2019.Utils
import parallel2019.Utils.withSocket
import java.net.ServerSocket
import java.net.Socket

class RequestHandler(private val socket: Socket) : Thread() {
    override fun run() {
        withSocket(socket) { request ->
            Thread.sleep(request.delay)
            val response = Utils.get(request.uri)
            Utils.createHttpResponse(response)
        }
    }
}

fun listen() {
    ServerSocket(Utils.PORT).use { serverSocket ->
        while (true) {
            val socket = serverSocket.accept()
            RequestHandler(socket).start()
        }
    }
}

fun main(args: Array<String>) = listen()

