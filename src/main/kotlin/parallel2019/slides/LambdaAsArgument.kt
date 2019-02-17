package parallel2019.slides.lasarg

fun repeat(n: Int, f: () -> Unit) {
    for (i in 0 until n) {
        f()
    }
}

fun main(args: Array<String>) {
    repeat(3, { println("A") })
    repeat(5) {
        println("B")
    }
}

