package cloud.dqn.utilities.project.models

import cloud.dqn.utilities.LongId
import cloud.dqn.utilities.project.models.factories.AmazonDynamoDBFactory
import cloud.dqn.utilities.project.models.response.DataResponse
import cloud.dqn.utilities.project.models.factories.InitParams
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable
import com.amazonaws.services.dynamodbv2.document.DynamoDB
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec
import com.amazonaws.services.dynamodbv2.model.*

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

    @DynamoDBAttribute(attributeName = DYN_CONST.COUNT_VALUE)
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
         * a new row is generated and initialized to a value of 1.
         * Atomically increments the count in the item
         */
        fun build(id: String, dynamoDB: DynamoDB): DataResponse<LongId?> {
            val updateItemSpec = UpdateItemSpec()
                    .withPrimaryKey(DYN_CONST.ID, id)
                    .withNameMap(hashMapOf("#c" to DYN_CONST.COUNT_VALUE))
                    .withValueMap(hashMapOf<String, Any>(":v" to 1))
                    .withUpdateExpression("add #c :v")
                    .withReturnValues(ReturnValue.ALL_NEW)
            val table = dynamoDB.getTable(DYN_CONST.TABLE_NAME)
            val updateItemOutcome = try {
                table.updateItem(updateItemSpec)
            } catch (e: Exception) {
                return DataResponse(exception = e)
            }
            val updateItemResult: UpdateItemResult = updateItemOutcome.updateItemResult
            val attributeValue: AttributeValue? = updateItemResult.attributes[DYN_CONST.COUNT_VALUE]

            return if (attributeValue == null) {
                DataResponse(error = "Response from table did not include attribute: ${DYN_CONST.COUNT_VALUE}")
            } else {
                val attributeValueAsLong = attributeValue.n?.toLongOrNull()
                if (attributeValueAsLong == null) {
                    DataResponse<LongId?>(error = "Response attribute ${DYN_CONST.COUNT_VALUE} was not convertable to a long")
                } else {
                    val longId = LongId(attributeValueAsLong)
                    if (!longId.validConversion()) {
                        DataResponse(data = longId, error = "LongId generated was not two way convertable")
                    } else {
                        DataResponse(data = longId)
                    }
                }
            }
        }

        fun createTable(initParams: InitParams = InitParams()): DataResponse<CreateTableResult?> {
            val amazonDynamoDB = AmazonDynamoDBFactory.build(initParams)
            return DynTable(amazonDynamoDB).create(DynIdGenerator::class.java)
        }

        fun getTableDescription(initParams: InitParams = InitParams()): DataResponse<TableDescription?> {
            val amazonDynamoDB = AmazonDynamoDBFactory.build(initParams)
            return DynTable(amazonDynamoDB).getDescription(DYN_CONST.TABLE_NAME)
        }

    }
}