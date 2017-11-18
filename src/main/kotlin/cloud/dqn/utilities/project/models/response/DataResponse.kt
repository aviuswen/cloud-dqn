package cloud.dqn.utilities.project.models.response

import com.google.gson.JsonObject

open class DataResponse<out T>: BaseResponse {
    val data: T?
    constructor(data: T? = null, error: String? = null, exception: Exception? = null)
            :super(error, exception) {
        this.data = data
    }

    override fun toJson(): JsonObject {
        val json = super.toJson()
        data?.let { json.addProperty("data", it.toString()) }
        return json
    }

    override fun toString(): String {
        return toJson().toString()
    }
}
