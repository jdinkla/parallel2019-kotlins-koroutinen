package parallel2019.ktor

import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.util.KtorExperimentalAPI
import kotlinx.coroutines.delay
import parallel2019.Request.Delay
import parallel2019.Utils
import io.ktor.client.engine.cio.CIO as clientCIO
import io.ktor.server.cio.CIO as serverCIO

// curl -v http://localhost:9090/1500/http://www.dinkla.net/en/index.html
// node_modules/.bin/loadtest -n 2000 -c 100 http://localhost:9090/250/http://localhost:8080

fun <T> List<T>.tail() = when (this.size) {
    0 -> listOf()
    else -> this.subList(1, this.size)
}

fun List<String>.joinWithSlashes(): String = when (this.size) {
    0 -> ""
    1 -> "${this[0]}//"
    else -> "${this[0]}//${this.tail().joinToString("/")}"
}

@KtorExperimentalAPI
val client = HttpClient(clientCIO) {
    engine {
        maxConnectionsCount = 5000 // Maximum number of socket connections.
        endpoint.apply {
            maxConnectionsPerRoute = 10000 // Maximum number of requests for a specific endpoint route.
            pipelineMaxSize = 20 // Max number of opened endpoints.
            keepAliveTime = 5000 // Max number of milliseconds to keep each connection alive.
            connectTimeout = 5000 // Number of milliseconds to wait trying to connect to the server.
            connectRetryAttempts = 5 // Maximum number of attempts for retrying a connection.
        }
    }
}

suspend fun delayAndGet(request: Delay): String {
    delay(request.delay)
    return client.get(request.uri)
}

fun ApplicationCall.createRequest(): Delay {
    val ms = this.parameters["ms"]!!.toLong()
    val parts = this.parameters.getAll("url")!!
    val uri = parts.joinWithSlashes()
    return Delay(uri, ms)
}

fun main() {
    val server = embeddedServer(serverCIO,
            configure = {
                connectionGroupSize = 250
            },
            port = Utils.PORT) {
        routing {
            get("/{ms}/{url...}") {
                val request = call.createRequest()
                val html = delayAndGet(request)
                call.respondText(html, ContentType.Text.Html)
            }
        }
    }
    server.start(wait = true)
}

