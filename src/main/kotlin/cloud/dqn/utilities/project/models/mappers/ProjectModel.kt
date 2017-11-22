package cloud.dqn.utilities.project.models.mappers

import cloud.dqn.utilities.project.models.factories.AmazonDynamoDBFactory
import cloud.dqn.utilities.project.models.factories.InitParams
import cloud.dqn.utilities.project.models.response.DataResponse
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB
import com.amazonaws.services.dynamodbv2.datamodeling.*
import com.amazonaws.services.dynamodbv2.model.*

/**
 * Access queries:
 *      Get most recent by updated for a given user
 *
 * Update queries:
 *      Create New
 *      Delete Project by Id
 *      Update metadata(!id && !user)
 */

@DynamoDBTable(tableName = ProjectModel.DYN_CONST.TABLE_NAME)
class ProjectModel : Metadata {

    constructor(): super()

    /* PRIMARY HASH */
    /**
     * Seccondary Hash
     *      super.id
     * GSI used instead of LSI as LSI is limited to 10GB in size
     *     super.updated
     *     super.created
     *
     *     GSI
     *      USER; created
     */

    @DynamoDBIndexHashKey(
            globalSecondaryIndexNames = arrayOf(DYN_CONST.GSI_CREATED, DYN_CONST.GSI_UPDATED))
    @DynamoDBHashKey()
    var user: String? = null

    /**
     * METADATA METHODS FOR DYNAMODB MAPPING
     */
    @DynamoDBRangeKey(attributeName = Metadata.DYN_CONST.ID)
    // @DynamoDBAttribute(attributeName = Metadata.DYN_CONST.ID)
    override fun dynId(): String? = super.dynId()
    override fun dynId(id: String?) { super.dynId(id) }

    @DynamoDBAttribute(attributeName = Metadata.DYN_CONST.NAME)
    override fun dynName(): String? = super.dynName()
    override fun dynName(name: String?) { super.dynName(name) }

    @DynamoDBAttribute(attributeName = Metadata.DYN_CONST.DESC)
    override fun dynDesc(): String? = super.dynDesc()
    override fun dynDesc(desc: String?) { super.dynDesc(desc) }

    @DynamoDBIndexRangeKey(
            attributeName = Metadata.DYN_CONST.CREATED,
            globalSecondaryIndexName = DYN_CONST.GSI_CREATED)
    @DynamoDBAttribute(attributeName = Metadata.DYN_CONST.CREATED)
    override fun dynCreated(): Int? = super.dynCreated()
    override fun dynCreated(created: Int?) { super.dynCreated(created) }

    @DynamoDBIndexRangeKey(
            attributeName = Metadata.DYN_CONST.UPDATED,
            globalSecondaryIndexName = DYN_CONST.GSI_UPDATED)
    @DynamoDBAttribute(attributeName = Metadata.DYN_CONST.UPDATED)
    override fun dynUpdated(): Int? = super.dynUpdated()
    override fun dynUpdated(updated: Int?) { super.dynUpdated(updated) }

    @DynamoDBAttribute(attributeName = Metadata.DYN_CONST.TAGS)
    override fun dynTags(): Set<String>? = super.dynTags()
    override fun dynTags(tags: Set<String>?) { super.dynTags(tags) }

    object DYN_CONST {
        const val TABLE_NAME = "p-Project"
        const val USER = "user"
        val KEY_SCHEMA = listOf(
                KeySchemaElement(DYN_CONST.USER, KeyType.HASH),
                KeySchemaElement(Metadata.DYN_CONST.ID, KeyType.RANGE)
        )
        const val GSI_UPDATED = "i-up"
        const val GSI_CREATED = "i-cr"
        val GSI_UPDATED_KEY_SCHEMA = listOf(
                KeySchemaElement(DYN_CONST.USER, KeyType.HASH),
                KeySchemaElement(Metadata.DYN_CONST.UPDATED, KeyType.RANGE)
        )
        val GSI_CREATED_KEY_SCHEMA = listOf(
                KeySchemaElement(DYN_CONST.USER, KeyType.HASH),
                KeySchemaElement(Metadata.DYN_CONST.CREATED, KeyType.RANGE)
        )
        val DEFAULT_PROVISIONING = ProvisionedThroughput(1L, 1L)
        val DEFAULT_ALL_PROJECTION: Projection = Projection().withProjectionType(ProjectionType.ALL)
    }

    companion object {
        /**
         * WARNING CREATE TABLE REQUEST WILL ALSO CREATED INDEXES IF ANNOTATED
         *  Sets provisionedThroughput = null
         *      and projection = KEYS_ONLY
         *  Also, current annotations for GSI IndexRangeKey do not generate
         *      @see https://github.com/aws/aws-sdk-java/issues/214
         *  Loading manually instead
         *  Range does not also load (bad annotations?)
         *  Attributes also do not load from mapper
         */
        fun createTableRequest(amazonDynamoDB: AmazonDynamoDB): CreateTableRequest {
            val gsiUpdated = GlobalSecondaryIndex()
                    .withIndexName(DYN_CONST.GSI_UPDATED)
                    .withKeySchema(DYN_CONST.GSI_UPDATED_KEY_SCHEMA)
                    .withProvisionedThroughput(DYN_CONST.DEFAULT_PROVISIONING)
                    .withProjection(DYN_CONST.DEFAULT_ALL_PROJECTION)
            val gsiCreated = GlobalSecondaryIndex()
                    .withIndexName(DYN_CONST.GSI_CREATED)
                    .withKeySchema(DYN_CONST.GSI_CREATED_KEY_SCHEMA)
                    .withProvisionedThroughput(DYN_CONST.DEFAULT_PROVISIONING)
                    .withProjection(DYN_CONST.DEFAULT_ALL_PROJECTION)
            return DynamoDBMapper(amazonDynamoDB)
                    .generateCreateTableRequest(ProjectModel::class.java)
                    .withKeySchema(DYN_CONST.KEY_SCHEMA)
                    .withAttributeDefinitions(
                            AttributeDefinition(Metadata.DYN_CONST.ID, ScalarAttributeType.S),
                            AttributeDefinition(Metadata.DYN_CONST.CREATED, ScalarAttributeType.N),
                            AttributeDefinition(Metadata.DYN_CONST.UPDATED, ScalarAttributeType.N)
                    )
                    .withProvisionedThroughput(DYN_CONST.DEFAULT_PROVISIONING)
                    // using specific method to write over rather than append
                    .withGlobalSecondaryIndexes(listOf(gsiUpdated, gsiCreated))

        }

        fun createTable(initParams: InitParams = InitParams()): DataResponse<CreateTableResult?> {
            val amazonDynamoDB = AmazonDynamoDBFactory.build(initParams)
            val createTableRequest = createTableRequest(amazonDynamoDB)
            return try {
                DataResponse(amazonDynamoDB.createTable(createTableRequest))
            } catch (e: Exception) {
                DataResponse(exception = e)
            }
        }
    }
}