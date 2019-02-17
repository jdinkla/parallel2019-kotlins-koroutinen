package parallel2019.slides

import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.CoroutineContext

fun CoroutineContext.elements(): List<CoroutineContext.Element> {
    return this.fold(listOf()) {
        ls, elem ->
        listOf(elem) + ls
    }
}

fun CoroutineScope.show(msg: String) {
    val s = this.coroutineContext.elements().joinToString("\n\t")
    println("$msg: ${Thread.currentThread().name}\n\t$s")
}
