package parallel2019.slides.lwr

class HTML {
    fun body() { }
}

fun html(init: HTML.() -> Unit): HTML {
    val html = HTML()
    html.init()
    return html
}

fun main(args: Array<String>) {
    html {
        body()
    }
}

// taken from https://kotlinlang.org/docs/reference/lambdas.html#function-literals-with-receiver

