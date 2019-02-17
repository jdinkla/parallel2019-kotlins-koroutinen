package parallel2019.slides.launchjob

import kotlinx.coroutines.*

fun main() = runBlocking {
    val job: Job = launch {
        delay(1000L)
        println("doing something")
    }
    job.cancel()
    job.join()
    println("active: ${job.isActive}")
    println("canceled: ${job.isCancelled}")
    println("completed: ${job.isCompleted}")
}

