//package parallel2019.slides.dp4b
//
//import kotlinx.coroutines.*
//import kotlinx.coroutines.future.asDeferred
//import kotlinx.coroutines.future.await
//import parallel2019.Request.Delay
//import parallel2019.Request.Error
//import parallel2019.Utils
//import parallel2019.Utils.client
//import parallel2019.Utils.withSocketSuspend
//import java.io.PrintWriter
//import java.net.ServerSocket
//import java.net.Socket
//import java.net.http.HttpResponse
//import java.util.concurrent.CompletableFuture
//
//suspend fun run(io: Socket) {
//    withSocketSuspend(io) { reader, writer ->
//        val request = Utils.parseRequest(reader)
//        val msg = when (request) {
//            is Delay -> delayAndGet(request)
//            is Error -> CompletableFuture.supplyAsync { Utils.RC_501 }
//        }
//        writer.println(msg.await())
//    }
//}
//
//suspend fun delayAndGet(request: Delay): Deferred<String> {
//    delay(request.delay)
//    val future = client.sendAsync(Utils.httpRequest(request.uri), bodyHandler)
//    val deferred = future.asDeferred()
//
//    deferred
//    val a = deferred.await()
//    return Utils.createHttpResponse(a)
//}
//
//private val bodyHandler = HttpResponse.BodyHandlers.ofString()!!
//
//fun main(args: Array<String>) {
//    ServerSocket(Utils.PORT).use { serverSocket ->
//        runBlocking {
//            while (true) {
//                val io = serverSocket.accept()
//                launch(Dispatchers.RequestIO) {
//                    run(io)
//                }
//            }
//        }
//    }
//}
