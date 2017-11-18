package cloud.dqn.utilities.project.models

open class Analysis {
    var difficulty: String = EMPTY_STR
    var time: Int = 0
    var notes: String = EMPTY_STR

    companion object {
        val EMPTY_STR = ""
    }
}