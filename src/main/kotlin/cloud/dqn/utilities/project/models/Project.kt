package cloud.dqn.utilities.project.models

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable

/**
 * Access queries:
 *      Get most recent by updated for a given user
 *
 * Update queries:
 *      Create New
 *      Delete Project by Id
 *      Update metadata(!id && !user)
 */

@DynamoDBTable(tableName = Project.DYN_CONST.DYN_TABLE_NAME)
class Project: Metadata() {

    /* PRIMARY HASH */
    var user: String? = null
    /**
     * Seccondary Hash
     *      super.id
     * GSI used instead of LSI as LSI is limited to 10GB in size
     *     super.updated
     *     super.created
     */

    @DynamoDBHashKey(attributeName = DYN_CONST.USER)
    fun dynUser(): String? = user
    fun dynUser(user: String?) { this.user = user }

    /**
     * METADATA METHODS FOR DYNAMODB MAPPING
     */
    @DynamoDBRangeKey(attributeName = Metadata.DYN_CONST.ID)
    override fun dynId(): String? = this.id
    override fun dynId(id: String?) { this.id = id }

    @DynamoDBAttribute(attributeName = Metadata.DYN_CONST.NAME)
    override fun dynName(): String? = this.name
    override fun dynName(name: String?) {this.name = name}

    @DynamoDBAttribute(attributeName = Metadata.DYN_CONST.DESC)
    override fun dynDesc(): String? = this.desc
    override fun dynDesc(desc: String?) { this.desc = desc }

    @DynamoDBAttribute(attributeName = Metadata.DYN_CONST.CREATED)
    override fun dynCreated(): Int? = this.created
    override fun dynCreated(created: Int?) { this.created = created }

    @DynamoDBAttribute(attributeName = Metadata.DYN_CONST.UPDATED)
    override fun dynUpdated(): Int? = this.updated
    override fun dynUpdated(updated: Int?) { this.updated = updated }

    @DynamoDBAttribute(attributeName = Metadata.DYN_CONST.TAGS)
    override fun dynTags(): Set<String>? = this.tags
    override fun dynTags(set: Set<String>?) { this.tags = tags }

    object DYN_CONST {
        const val DYN_TABLE_NAME = "p-Project"
        const val USER = "user"

    }
}