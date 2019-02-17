package parallel2019.slides.channel

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() = runBlocking<Unit> {
    val channel = Channel<Int>()
    launch {
        for (v in 1..5) {
            println("Sending $v")
            channel.send(v)
        }
    }
    launch {
        repeat(5) {
            val v = channel.receive()
            println("Receiving $v")
        }
    }
}

