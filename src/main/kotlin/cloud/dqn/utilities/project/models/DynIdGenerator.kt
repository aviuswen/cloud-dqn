package cloud.dqn.utilities.project.models

import cloud.dqn.utilities.LongId
import cloud.dqn.utilities.project.models.singletons.DynAmazonDynamoDB
import cloud.dqn.utilities.project.models.singletons.DynClientSingleton
import cloud.dqn.utilities.project.models.singletons.DynamoDBMapperSingleton
import cloud.dqn.utilities.project.models.singletons.InitParams
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable
import com.amazonaws.services.dynamodbv2.document.UpdateItemOutcome
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec
import com.amazonaws.services.dynamodbv2.model.AttributeValue
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput
import com.amazonaws.services.dynamodbv2.model.ReturnValue
import com.amazonaws.services.dynamodbv2.model.UpdateItemResult

/**
 * Represents a table in DynamoDB in order to generate
 * Ids for a specific table (outside of this one).
 *
 * This classes table stores names of other tables or subtables("tableName:subNamespace")
 *  and a counter for that namespace.  Count is incremented atomically
 *  for each call to factory.
 */

@DynamoDBTable(tableName = DynIdGenerator.DYN_CONST.TABLE_NAME)
class DynIdGenerator {

    @DynamoDBHashKey(attributeName = DYN_CONST.ID)
    var id: String? = null

    @DynamoDBRangeKey(attributeName = DYN_CONST.COUNT_VALUE)
    var count: Long? = null

    object DYN_CONST {
        const val TABLE_NAME = "p-Id"
        const val ID = "id"
        const val COUNT_VALUE = "cValue"
    }

    companion object {
        /**
         * Generates a new stringId based upon the item corresponding
         * to the @param id;  If id does not exist in the table yet,
         * a new row is generated and initialized to a value of 1
         *
         * TODO: handle exceptions at updateItem
         */
        fun factory(id: String): String? {
            val updateItemSpec = UpdateItemSpec()
                    .withPrimaryKey(DYN_CONST.ID, id)
                    .withNameMap(hashMapOf("#c" to DYN_CONST.COUNT_VALUE))
                    .withValueMap(hashMapOf<String, Any>(":v" to 1))
                    .withUpdateExpression("set #p = #p + :v")
                    .withReturnValues(ReturnValue.ALL_NEW)
            val table = DynClientSingleton
                    .getInstance()
                    .client
                    .getTable(DYN_CONST.TABLE_NAME)
            val updateItemOutcome: UpdateItemOutcome = table.updateItem(updateItemSpec)
            val updateItemResult: UpdateItemResult = updateItemOutcome.updateItemResult
            val attributes: Map<String, AttributeValue> = updateItemResult.attributes
            val attributeValue: AttributeValue? = attributes[DYN_CONST.COUNT_VALUE]
            return attributeValue?.let {
                val attributeValueAsLong = it.n?.toLongOrNull()
                attributeValueAsLong?.let {
                    val longId = LongId(it)
                    longId.str
                }
            }
        }

        fun createTable(initParams: InitParams?) {
            val mapper = DynamoDBMapperSingleton.getInstance().mapper
            val createTableRequest = mapper.generateCreateTableRequest(DynIdGenerator::class.java)
            createTableRequest.provisionedThroughput =
                    ProvisionedThroughput(1L, 1L)
            val amazonDynamoDB = DynAmazonDynamoDB.factory(initParams)

            amazonDynamoDB.createTable(createTableRequest)
        }

    }
}