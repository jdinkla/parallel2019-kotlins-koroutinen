package parallel2019.slides.dp2ex

import parallel2019.Request.Delay
import parallel2019.Utils
import java.net.ConnectException
import java.net.ServerSocket
import java.net.Socket
import java.util.concurrent.Executors

class RequestHandler(private val socket: Socket) : Thread() {
    override fun run() {
        Utils.withSocket(socket) { request -> delayAndGetAndHandleExceptions(request) }
    }
}

fun delayAndGetAndHandleExceptions(request: Delay): String {
    Thread.sleep(request.delay)
    var result = ""
    try {
        val response = Utils.get(request.uri)
        result = Utils.createHttpResponse(response)
    } catch (e: ConnectException) {
        Utils.print("EXCEPTION ${e.message}")
    }
    return result
}

fun main(args: Array<String>) {
    val pool = Executors.newWorkStealingPool()
    ServerSocket(Utils.PORT).use { serverSocket ->
        while (true) {
            val socket = serverSocket.accept()
            pool.execute {
                RequestHandler(socket).run()
            }
        }
    }
}

