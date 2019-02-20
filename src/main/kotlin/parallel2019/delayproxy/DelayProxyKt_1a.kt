package parallel2019.delayproxy_1a

import parallel2019.Request.Error
import parallel2019.Request.Delay
import parallel2019.Utils
import java.net.ServerSocket
import java.net.Socket

class RequestHandler(private val socket: Socket) : Thread() {
    override fun run() {
        val theReader = Utils.newReader(socket)
        val theWriter = Utils.newWriter(socket)
        theReader.use { reader ->
            theWriter.use { writer ->
                val request = Utils.parseRequest(reader)
                val response = when (request) {
                    is Delay -> delayAndGet(request)
                    is Error -> Utils.RC_501
                }
                writer.println(response)
            }
        }
    }
}

fun delayAndGet(request: Delay): String {
    Thread.sleep(request.delay)
    val response= Utils.get(request.uri)
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