package cloud.dqn.utilities.project.models.mappers

abstract class Metadata {
    protected var id: String? = null
    protected var name: String? = null
    protected var desc: String? = null
    protected var created: Int? = null
    protected var updated: Int? = null
    protected var tags: Set<String>? = null

    open fun dynId(): String? = id
    open fun dynId(id: String?) { this.id = id }

    open fun dynName(): String? = name
    open fun dynName(name: String?) { this.name = name }

    open fun dynDesc(): String? = desc
    open fun dynDesc(desc: String?) { this.desc = desc }

    open fun dynCreated(): Int? = created
    open fun dynCreated(created: Int?) { this.created = created }

    open fun dynUpdated(): Int? = updated
    open fun dynUpdated(updated: Int?) { this.updated = updated }

    open fun dynTags(): Set<String>? = tags
    open fun dynTags(tags: Set<String>?) { this.tags = tags }

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