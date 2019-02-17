package parallel2019

class App {
    val greeting: String
        get() {
            return "Hello parallel 2019!"
        }
}

fun main(args: Array<String>) {
    println(App().greeting)
}
