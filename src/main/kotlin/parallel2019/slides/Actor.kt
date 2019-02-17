package parallel2019.slides.actor

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.runBlocking
import parallel2019.slides.actor.Msg.Inc
import parallel2019.slides.actor.Msg.Read

sealed class Msg {
    object Inc : Msg()
    class Read(val response: CompletableDeferred<Int>) : Msg()
}

@ObsoleteCoroutinesApi
fun CoroutineScope.counterActor() = actor<Msg> {
    var counter = 0
    for (msg in channel) {
        when (msg) {
            is Inc -> counter++
            is Read -> msg.response.complete(counter)
        }}}

@ObsoleteCoroutinesApi
fun main() = runBlocking {
    val actor = counterActor()
    println("counter at start: ${getCounterValue(actor)}")
    actor.send(Inc)
    actor.send(Inc)
    println("counter at end: ${getCounterValue(actor)}")
}

suspend fun getCounterValue(actor: SendChannel<Msg>): Int {
    val num = CompletableDeferred<Int>()
    actor.send(Read(num))
    return num.await()
}

