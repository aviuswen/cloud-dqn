package cloud.dqn.utilities.project.models

import cloud.dqn.utilities.project.models.response.DataResponse
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper
import com.amazonaws.services.dynamodbv2.document.DynamoDB
import com.amazonaws.services.dynamodbv2.model.CreateTableResult
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput
import com.amazonaws.services.dynamodbv2.model.TableDescription

/**
 * Code formatting is different yes; trying it out
 *
 * Example Usage:
 *    DynTable(amazonDynamoDB).create(DynIdGenerator::class.java)
 */
class DynTable(val amazonDynamoDB: AmazonDynamoDB) {
    /**
     * This is a basic table creation
     *  Does not include index creation and throughput for gsi(s)
     *      and all the othe bells and whistles
     * For more specifications for creating tables
     *  @see com.amazonaws.services.dynamodbv2.model.CreateTableRequest
     */
    fun create(
            clazz: Class<*>,
            readCapacity: Long = 1L,
            writeCapacity: Long = 1L
    ): DataResponse<CreateTableResult?> {
        val createTableRequest = DynamoDBMapper(amazonDynamoDB)
                .generateCreateTableRequest(clazz)
        createTableRequest.provisionedThroughput = ProvisionedThroughput(
                if (readCapacity <= 0) DEFAULT_READ_CAPACITY else readCapacity,
                if (writeCapacity <= 0) DEFAULT_WRITE_CAPACITY else writeCapacity
        )
        return try {
            DataResponse(amazonDynamoDB.createTable(createTableRequest))
        } catch (e: Exception) {
            DataResponse(exception = e)
        }
    }

    fun getDescription(
            tableName: String
    ): DataResponse<TableDescription?> {
        return try {
            DataResponse(DynamoDB(amazonDynamoDB).getTable(tableName).describe())
        } catch (e: Exception) {
            DataResponse(exception = e)
        }
    }
    companion object {
        private val DEFAULT_READ_CAPACITY = 1L
        private val DEFAULT_WRITE_CAPACITY = 1L
    }
}