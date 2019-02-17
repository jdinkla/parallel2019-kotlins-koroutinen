package parallel2019.slides.prod

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.runBlocking

@ExperimentalCoroutinesApi
fun CoroutineScope.integers() = produce {
    for (v in 1..5) {
        println("Sending $v")
        send(v)
    }
}

@ExperimentalCoroutinesApi
fun main() = runBlocking {
    for (v in integers()) {
        println("For $v")
    }
}


