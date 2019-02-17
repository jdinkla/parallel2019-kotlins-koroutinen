package parallel2019.context

import kotlinx.coroutines.*
import parallel2019.slides.show

@ObsoleteCoroutinesApi
val pool = newFixedThreadPoolContext(4, "my pool")

val handler = CoroutineExceptionHandler() { context, ex ->
    println("Gotcha $ex")
}

fun main() = runBlocking<Unit> {
    launch(handler + pool) {
        show("launch #1")
        launch {
            show("launch #2")
        }
        withContext(NonCancellable + Dispatchers.IO) {
            show("withContext")
        }
    }
}


