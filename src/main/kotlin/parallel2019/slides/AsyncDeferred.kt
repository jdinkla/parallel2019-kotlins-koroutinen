package parallel2019.slides.asyncdef

import kotlinx.coroutines.*

fun main() = runBlocking {
    val deferred = async {
        delay(1000L)
        "returns a string"
    }
    val result = deferred.await()
    println("active: ${deferred.isActive}")
    println("canceled: ${deferred.isCancelled}")
    println("completed: ${deferred.isCompleted}")
}

