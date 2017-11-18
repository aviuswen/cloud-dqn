package cloud.dqn.utilities.project.models.factories

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB
import com.amazonaws.services.dynamodbv2.document.DynamoDB

object DynamoDBFactory {
    fun build(initParams: InitParams = InitParams()): DynamoDB {
        return DynamoDB(AmazonDynamoDBFactory.build(initParams))
    }
    fun build(amazonDynamoDB: AmazonDynamoDB): DynamoDB {
        return DynamoDB(amazonDynamoDB)
    }
}