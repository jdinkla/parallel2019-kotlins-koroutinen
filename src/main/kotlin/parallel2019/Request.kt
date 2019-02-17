package parallel2019

import java.net.URI
import java.net.http.HttpRequest
import java.util.regex.Pattern

sealed class Request {
    companion object {
        fun parse(requestLine: String): Request {
            val parts = splitRequestLine(requestLine)
            if (parts.size != 3) {
                return Error
            }
            val (verb, uri, _) = parts
            return if ("GET" == verb) {
                createRequestFromUri(verb, uri)
            } else {
                Error
            }
        }

        internal fun createRequestFromUri(verb: String, uri: String): Request {
            val pattern = Pattern.compile("^\\/([0-9]+)\\/(.*)$")
            val matcher = pattern.matcher(uri)
            return if (matcher.find()) {
                val delay = java.lang.Long.valueOf(matcher.group(1))
                val realUri = matcher.group(2)
                Delay(verb, realUri, delay)
            } else {
                Error
            }
        }

        internal fun splitRequestLine(requestLine: String) = requestLine
            .split(" ".toRegex())
            .dropLastWhile { it.isEmpty() }
    }

    object Error : Request()

    data class Delay(val verb: String,
                     val uri: String,
                     val delay: Long) : Request() {
        fun get() = HttpRequest.newBuilder()
            .uri(URI.create(uri))
            .GET()
            .build()!!
    }
}

