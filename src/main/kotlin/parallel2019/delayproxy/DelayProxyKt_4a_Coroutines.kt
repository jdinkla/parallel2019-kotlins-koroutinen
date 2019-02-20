package parallel2019.delayproxy_4

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.future.await
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import parallel2019.Request.Delay
import parallel2019.Request.Error
import parallel2019.Utils
import parallel2019.Utils.client
import parallel2019.Utils.withSocketSuspend
import java.net.ServerSocket
import java.net.Socket
import java.net.http.HttpResponse
import java.util.concurrent.CompletableFuture

suspend fun run(socket: Socket) {
    withSocketSuspend(socket) { reader, writer ->
        val request = Utils.parseRequest(reader)
        val msg = when (request) {
            is Delay -> delayAndGet(request)
            is Error -> CompletableFuture.supplyAsync { Utils.RC_501 }
        }
        writer.println(msg.await())
    }
}

fun delayAndGet(request: Delay): CompletableFuture<String> {
    return CompletableFuture.runAsync {
        Thread.sleep(request.delay)
    }.thenCompose {
        client.sendAsync(Utils.httpRequest(request.uri), bodyHandler)
    }.thenApply { response ->
        Utils.createHttpResponse(response)
    }
}

private val bodyHandler = HttpResponse.BodyHandlers.ofString()!!

fun main(args: Array<String>) {
    ServerSocket(Utils.PORT).use { serverSocket ->
        runBlocking {
            while (true) {
                val socket = serverSocket.accept()
                launch(Dispatchers.IO) {
                    run(socket)
                }
            }
        }
    }
}
