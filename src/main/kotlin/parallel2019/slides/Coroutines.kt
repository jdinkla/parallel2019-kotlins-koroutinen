package parallel2019.slides.coroutines

import kotlinx.coroutines.*

fun a() = 1

fun b() {}

fun CoroutineScope.foo(x: Int): Deferred<Int> = async { 3 }

fun CoroutineScope.bar(x: Int, y: Int): Deferred<Int> = async { 3 }

fun c(x: Int) {}

fun main() = runBlocking {
    val a = a()
    val y =foo(a).await()
    b()
    val z = bar(a, y).await() // suspension point #2
    c(z)
}
