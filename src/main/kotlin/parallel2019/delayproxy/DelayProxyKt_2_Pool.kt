package parallel2019.delayproxy_2

import parallel2019.Request.Delay
import parallel2019.Utils
import java.net.ServerSocket
import java.net.Socket
import java.util.concurrent.Executors

class RequestHandler(private val socket: Socket) : Runnable {
    override fun run() {
        Utils.withSocket(socket) { request -> delayAndGet(request) }
    }
}

fun delayAndGet(request: Delay): String {
    Thread.sleep(request.delay)
    val response = Utils.get(request.uri)
    return Utils.createHttpResponse(response)
}

fun main(args: Array<String>) {
    //val pool = Executors.newWorkStealingPool()
    val pool = Executors.newCachedThreadPool()
    ServerSocket(Utils.PORT).use { serverSocket ->
        while (true) {
            val socket = serverSocket.accept()
            pool.execute {
                RequestHandler(socket).run()
            }
        }
    }
}

