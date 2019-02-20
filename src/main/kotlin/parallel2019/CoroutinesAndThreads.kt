package parallel2019.slides.cat

import kotlinx.coroutines.*
import parallel2019.slides.thread

suspend fun makeSomeNoise() = coroutineScope {
    (1..100).toList().map {
        launch {
            delay(1000L)
        }
    }.joinAll()
}

fun main() = runBlocking(Dispatchers.Default) {
    val j = launch {
        for (i in 1..10) {
            thread("$i. running on thread ")
            delay(1000L)
        }
    }
    makeSomeNoise()
    j.join()
}
