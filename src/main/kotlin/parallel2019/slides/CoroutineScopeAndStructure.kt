package parallel2019.slides.coroscostruc

import kotlinx.coroutines.*
import parallel2019.slides.show

fun main() = runBlocking<Unit> {

    SupervisorJob()

    launch {
        show("launch #1")
        launch {
            show("launch #A")
        }
        launch {
            show("launch #B")
            launch {
                show("launch #B2")
            }
        }
    }
}


