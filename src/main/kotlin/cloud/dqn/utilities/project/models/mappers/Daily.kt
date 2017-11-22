package cloud.dqn.utilities.project.models.mappers

import com.amazonaws.services.dynamodbv2.datamodeling.*

@DynamoDBTable(tableName = Daily.DYN_CONST.TABLE_NAME)
class Daily: Metadata {

    constructor(): super()
    @DynamoDBIndexHashKey(
            attributeName = DYN_CONST.SPRINT_ID,
            globalSecondaryIndexName = DYN_CONST.GSI_SPRINT_TO_DATE)
    @DynamoDBHashKey(attributeName = DYN_CONST.SPRINT_ID)
    var sprintId: String? = null        // Primary hash

    @DynamoDBIndexRangeKey(
            globalSecondaryIndexNames = arrayOf(
                    DYN_CONST.GSI_SPRINT_TO_DATE,
                    DYN_CONST.GSI_PROJECT_TO_DATE,
                    DYN_CONST.GSI_USER_TO_DATE) )
    var date: Int? = null               // GSI sprintId; date

    @DynamoDBIndexHashKey(
            globalSecondaryIndexName = DYN_CONST.GSI_USER_TO_DATE)
    var user: String? = null            // GSI user; date

    @DynamoDBIndexHashKey(
            attributeName = DYN_CONST.PROJECT_ID,
            globalSecondaryIndexName = DYN_CONST.GSI_PROJECT_TO_DATE)
    @DynamoDBAttribute(attributeName = DYN_CONST.PROJECT_ID)
    var projectId: String? = null       // GSI projectId: date

    // Other Attributes
    @DynamoDBAttribute() var timeStart: Int? = null
    @DynamoDBAttribute() var timeEnd: Int? = null
    @DynamoDBAttribute() var difficulty: String? = null
    @DynamoDBAttribute() var time: Int? = null
    @DynamoDBAttribute() var notes: String? = null

    /**
     * METADATA METHODS FOR DYNAMODB MAPPING
     */
    @DynamoDBRangeKey(attributeName = Metadata.DYN_CONST.ID)
    override fun dynId(): String? = super.dynId()
    override fun dynId(id: String?) { super.dynId(id) }

    @DynamoDBAttribute(attributeName = Metadata.DYN_CONST.NAME)
    override fun dynName(): String? = super.dynName()
    override fun dynName(name: String?) { super.dynName(name) }

    @DynamoDBAttribute(attributeName = Metadata.DYN_CONST.DESC)
    override fun dynDesc(): String? = super.dynDesc()
    override fun dynDesc(desc: String?) { super.dynDesc(desc) }

    @DynamoDBAttribute(attributeName = Metadata.DYN_CONST.CREATED)
    override fun dynCreated(): Int? = super.dynCreated()
    override fun dynCreated(created: Int?) { super.dynCreated(created) }

    @DynamoDBAttribute(attributeName = Metadata.DYN_CONST.UPDATED)
    override fun dynUpdated(): Int? = super.dynUpdated()
    override fun dynUpdated(updated: Int?) { super.dynUpdated(updated) }

    @DynamoDBAttribute(attributeName = Metadata.DYN_CONST.TAGS)
    override fun dynTags(): Set<String>? = super.dynTags()
    override fun dynTags(tags: Set<String>?) { super.dynTags(tags) }


    object DYN_CONST {
        const val TABLE_NAME = "p-Daily"
        const val SPRINT_ID = "sprint-id"
        const val PROJECT_ID = "project-id"
        const val GSI_SPRINT_TO_DATE = "i-s-d"
        const val GSI_USER_TO_DATE = "i-u-d"
        const val GSI_PROJECT_TO_DATE = "i-p-d"
    }

}