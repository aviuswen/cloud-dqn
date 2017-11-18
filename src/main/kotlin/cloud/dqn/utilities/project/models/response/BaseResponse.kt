package cloud.dqn.utilities.project.models.response

import com.google.gson.JsonObject

open class BaseResponse {
    var error: String?
    var exception: Exception?

    constructor(error: String? = null, exception: Exception? = null) {
        this.error = error
        this.exception = exception
    }

    fun success(): Boolean = error == null && exception == null
    fun failure(): Boolean = error != null || exception != null

    override fun toString(): String {
        return toJson().toString()
    }

    open fun toJson(): JsonObject {
        val json = JsonObject()
        error?.let { json.addProperty("error", it) }
        exception?.let { json.addProperty("exception", it.toString())}
        return json
    }
}