package cloud.dqn.utilities.project.models

import com.fasterxml.jackson.core.JsonEncoding

open class Analysis {
    var difficulty: String = EMPTY_STR
    var time: Int = 0
    var notes: String = EMPTY_STR

    constructor(json: JsonEncoding)

    companion object {
        val EMPTY_STR = ""
    }
}