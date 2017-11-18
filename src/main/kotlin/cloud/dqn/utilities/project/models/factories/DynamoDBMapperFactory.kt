package cloud.dqn.utilities.project.models.factories

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper

object DynamoDBMapperFactory {
    fun build(initParams: InitParams = InitParams()): DynamoDBMapper {
        return DynamoDBMapper(AmazonDynamoDBFactory.build(initParams))
    }
    fun build(amazonDynamoDB: AmazonDynamoDB): DynamoDBMapper {
        return DynamoDBMapper(amazonDynamoDB)
    }
}