package parallel2019.slides

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.launch

fun CoroutineScope.workAsync() = launch {
    // do something here
}

fun CoroutineScope.getAsync() = async {
    42
}

fun main() = runBlocking {
    val job = workAsync()
    val result = getAsync()
    result.await()
    job.join()
}

