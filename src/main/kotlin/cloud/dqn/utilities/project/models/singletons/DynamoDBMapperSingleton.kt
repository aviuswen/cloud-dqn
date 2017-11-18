package cloud.dqn.utilities.project.models.singletons

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper

class DynamoDBMapperSingleton private constructor(aws: InitParams? = null) {
    val mapper: DynamoDBMapper

    init {
        val amazonDynamoDB: AmazonDynamoDB = DynAmazonDynamoDB.factory(aws)
        this.mapper = DynamoDBMapper(amazonDynamoDB)
    }

    companion object: SingletonHolder<DynamoDBMapperSingleton, InitParams?>(::DynamoDBMapperSingleton)
}