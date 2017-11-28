package cloud.dqn.utilities.project.models.mappers

import cloud.dqn.utilities.project.models.factories.AmazonDynamoDBFactory
import cloud.dqn.utilities.project.models.factories.InitParams
import cloud.dqn.utilities.project.models.response.DataResponse
import com.amazonaws.services.dynamodbv2.datamodeling.*
import com.amazonaws.services.dynamodbv2.model.*

@DynamoDBTable(tableName = DailyModel.DYN_CONST.TABLE_NAME)
class DailyModel : Metadata {

    constructor(): super()
    @DynamoDBIndexHashKey(
            attributeName = DYN_CONST.SPRINT_ID,
            globalSecondaryIndexName = DYN_CONST.GSI_SPRINT_TO_DATE)
    @DynamoDBHashKey(attributeName = DYN_CONST.SPRINT_ID)
    var sprintId: String? = null        // Primary hash

    @DynamoDBIndexRangeKey(
            attributeName = DYN_CONST.DATE,
            globalSecondaryIndexNames = arrayOf(
                    DYN_CONST.GSI_SPRINT_TO_DATE,
                    DYN_CONST.GSI_PROJECT_TO_DATE,
                    DYN_CONST.GSI_USER_TO_DATE) )
    var date: Int? = null               // GSI sprintId; date

    @DynamoDBIndexHashKey(
            attributeName = DYN_CONST.USER,
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
        const val DATE = "date"
        const val USER = "user"
        val KEY_SCHEMA = listOf(
                KeySchemaElement(SPRINT_ID, KeyType.HASH),
                KeySchemaElement(Metadata.DYN_CONST.ID, KeyType.RANGE)
        )

        const val GSI_SPRINT_TO_DATE = "i-s-d"
        val GSI_SPRINT_KEY = listOf(
                KeySchemaElement(SPRINT_ID, KeyType.HASH),
                KeySchemaElement(DATE, KeyType.RANGE)
        )

        const val GSI_USER_TO_DATE = "i-u-d"
        val GSI_USER_KEY = listOf(
                KeySchemaElement(USER, KeyType.HASH),
                KeySchemaElement(DATE, KeyType.RANGE)
        )

        const val GSI_PROJECT_TO_DATE = "i-p-d"
        val GSI_PROJECT_KEY = listOf(
                KeySchemaElement(PROJECT_ID, KeyType.HASH),
                KeySchemaElement(DATE, KeyType.RANGE)
        )

        val ATTRIBUTE_DEF = arrayOf(
                AttributeDefinition(Metadata.DYN_CONST.ID, ScalarAttributeType.S),
                AttributeDefinition(DATE, ScalarAttributeType.N),
                AttributeDefinition(USER, ScalarAttributeType.S),
                AttributeDefinition(PROJECT_ID, ScalarAttributeType.S)
        )
    }

    companion object {
        // TODO TEST
        fun createTable(
                initParams: InitParams = InitParams()
        ): DataResponse<CreateTableResult?> {
            val provisioning = ProvisionedThroughput(1L, 1L)
            val gsi0 = GlobalSecondaryIndex()
                    .withIndexName(DYN_CONST.GSI_SPRINT_TO_DATE)
                    .withKeySchema(DYN_CONST.GSI_SPRINT_KEY)



            val amz = AmazonDynamoDBFactory.build(initParams)
            val createTableRequest = DynamoDBMapper(amz)
                    .generateCreateTableRequest(DailyModel::class.java)
                    .withKeySchema(DYN_CONST.KEY_SCHEMA)
                    .withAttributeDefinitions(*(DYN_CONST.ATTRIBUTE_DEF)) // appending version



            TODO("finish")
        }
    }

}