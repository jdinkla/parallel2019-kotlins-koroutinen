package parallel2019.slides.dp3

import parallel2019.Request.Delay
import parallel2019.Request.Error
import parallel2019.Utils
import parallel2019.Utils.client
import parallel2019.Utils.withSocket
import java.io.PrintWriter
import java.net.ServerSocket
import java.net.Socket
import java.net.http.HttpResponse
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors

class RequestHandler(private val socket: Socket) : Runnable {
    override fun run() {
        withSocket(socket) { reader, writer ->
            val request = Utils.parseRequest(reader)
            when (request) {
                is Delay -> asyncDelayAndGet(request, writer).join()
                is Error -> writer.println(Utils.RC_501)
            }
        }
    }
}

private fun asyncDelayAndGet(request: Delay,
                             writer: PrintWriter): CompletableFuture<Void> {
    return CompletableFuture.runAsync {
        Thread.sleep(request.delay)
    }.thenCompose {
        client.sendAsync(Utils.httpRequest(request.uri), bodyHandler)
    }.thenApply { response ->
        Utils.createHttpResponse(response)
    }.thenAccept { msg ->
        writer.println(msg)
    }
}

private val bodyHandler = HttpResponse.BodyHandlers.ofString()!!

fun main(args: Array<String>) {
    // val pool = Executors.newWorkStealingPool()
    val pool = Executors.newFixedThreadPool(1000)
    ServerSocket(Utils.PORT).use { serverSocket ->
        while (true) {
            val socket = serverSocket.accept()
            pool.execute {
                RequestHandler(socket).run()
            }
        }
    }
}
