package cloud.dqn.utilities.project.models.mappers

import cloud.dqn.utilities.project.models.factories.AmazonDynamoDBFactory
import cloud.dqn.utilities.project.models.factories.InitParams
import cloud.dqn.utilities.project.models.response.DataResponse
import com.amazonaws.services.dynamodbv2.datamodeling.*
import com.amazonaws.services.dynamodbv2.model.*

/**
 * TODO SET SOFT LIMITS ON LENGTHS
 */
@DynamoDBTable(tableName = SprintModel.DYN_CONST.TABLE_NAME)
class SprintModel : Metadata {

    constructor(): super()

    @DynamoDBIndexHashKey(
            attributeName = DYN_CONST.PROJECT_ID,
            globalSecondaryIndexName = DYN_CONST.GSI_PROJECT_ID_TO_START)
    @DynamoDBHashKey(attributeName = DYN_CONST.PROJECT_ID)
    var projectId: String? = null   // id is range

    @DynamoDBAttribute() var username: String? = null    // NOT GSI FOR NOW

    @DynamoDBIndexRangeKey(
            attributeName = DYN_CONST.START,
            globalSecondaryIndexName = DYN_CONST.GSI_PROJECT_ID_TO_START)
    @DynamoDBAttribute(attributeName = DYN_CONST.START)
    var start: Int? = null

    @DynamoDBAttribute() var end: Int? = null

    /* ANALYSIS SECTION */
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
        const val TABLE_NAME = "p-Sprint"
        const val PROJECT_ID = "project-id"
        val KEY_SCHEMA = listOf(
                KeySchemaElement(DYN_CONST.PROJECT_ID, KeyType.HASH),
                KeySchemaElement(Metadata.DYN_CONST.ID, KeyType.RANGE)
        )
        const val START = "start"
        const val GSI_PROJECT_ID_TO_START = "i-p-s"
        val GSI_PROJECT_ID_TO_START_KEY_SCHEMA = listOf (
                KeySchemaElement(DYN_CONST.PROJECT_ID, KeyType.HASH),
                KeySchemaElement(DYN_CONST.START, KeyType.RANGE)
        )

    }

    companion object {
        private val MAX_ANALYSIS_LENGTH = 512

        // TODO TEST
        fun createTable(
                initParams: InitParams = InitParams()
        ): DataResponse<CreateTableResult?> {
            val provisioning = ProvisionedThroughput(1L, 1L)

            val gsi = GlobalSecondaryIndex()
                    .withIndexName(DYN_CONST.GSI_PROJECT_ID_TO_START)
                    .withProjection(Projection().withProjectionType(ProjectionType.ALL))
                    .withProvisionedThroughput(provisioning)
                    .withKeySchema(DYN_CONST.GSI_PROJECT_ID_TO_START_KEY_SCHEMA)

            val amz = AmazonDynamoDBFactory.build(initParams)
            val createTableRequest = DynamoDBMapper(amz)
                    .generateCreateTableRequest(SprintModel::class.java)
                    .withKeySchema(DYN_CONST.KEY_SCHEMA)
                    .withAttributeDefinitions(
                            AttributeDefinition(Metadata.DYN_CONST.ID, ScalarAttributeType.S),
                            AttributeDefinition(DYN_CONST.START, ScalarAttributeType.N) )
                    .withProvisionedThroughput(provisioning)
                    .withGlobalSecondaryIndexes(gsi)
            return try {
                DataResponse(data = amz.createTable(createTableRequest))
            } catch (e: Exception) {
                DataResponse(exception = e)
            }
        }
    }
}

