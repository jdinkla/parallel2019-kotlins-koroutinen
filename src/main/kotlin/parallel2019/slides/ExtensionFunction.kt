package parallel2019.slides.extfun

fun traditional(string: String): Boolean {
    return string.startsWith("A")
}

fun short(string: String): Boolean = string.startsWith("A")

fun String.isStartingWithA(): Boolean = this.startsWith("A")

fun main(args: Array<String>) {
    traditional("ABC")
    short("ABC")
    "ABC".isStartingWithA()
}

