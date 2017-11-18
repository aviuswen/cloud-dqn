package cloud.dqn.utilities.project.models

abstract class Metadata {
    protected var id: String? = null
    protected var name: String? = null
    protected var desc: String? = null
    protected var created: Int? = null
    protected var updated: Int? = null
    protected var tags: Set<String>? = null

    abstract fun dynId(): String?
    abstract fun dynId(id: String?)

    abstract fun dynName(): String?
    abstract fun dynName(name: String?)

    abstract fun dynDesc(): String?
    abstract fun dynDesc(desc: String?)

    abstract fun dynCreated(): Int?
    abstract fun dynCreated(created: Int?)

    abstract fun dynUpdated(): Int?
    abstract fun dynUpdated(updated: Int?)

    abstract fun dynTags(): Set<String>?
    abstract fun dynTags(set: Set<String>?)

    companion object {
        val EMPTY_STR = ""
        val MAX_DESCRIPTION = 512 // chars
    }
    object DYN_CONST {
        const val ID = "id"
        const val NAME = "name"
        const val DESC = "desc"
        const val CREATED = "created"
        const val UPDATED = "update"
        const val TAGS = "tags"
    }
}