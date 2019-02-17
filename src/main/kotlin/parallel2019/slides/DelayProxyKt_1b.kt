package parallel2019.slides.dp1b

import parallel2019.Request.Error
import parallel2019.Request.Delay
import parallel2019.Utils
import parallel2019.Utils.withSocket
import java.net.ServerSocket
import java.net.Socket

class RequestHandler(private val socket: Socket) : Thread() {
    override fun run() {
        withSocket(socket) { reader, writer ->
            val request = Utils.parseRequest(reader)
            val response = when (request) {
                is Delay -> delayAndGet(request)
                is Error -> Utils.RC_501
            }
            writer.println(response)
        }
    }
}

fun delayAndGet(request: Delay): String {
    Thread.sleep(request.delay)
    val response = Utils.get(request.uri)
    return Utils.createHttpResponse(response)
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