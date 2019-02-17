package parallel2019.slides.wc

import kotlinx.coroutines.*
import parallel2019.slides.show


fun main() = runBlocking {
    show("A")
    launch {
        show("B")
        delay(1000L)
        show("B 2")
    }
    show("C")
    withContext(Dispatchers.IO) {
        show("D")
        delay(1000L)
        show("D 2")
    }
    show("E")
}
