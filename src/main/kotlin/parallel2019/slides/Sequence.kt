package parallel2019.slides.seq

val seq = sequence {
    var i = 0
    while (true) {
        println("Generate $i")
        yield(i++)
    }
}

fun main() {
    seq.take(3).forEach {
        println("Receive $it")
    }
}

