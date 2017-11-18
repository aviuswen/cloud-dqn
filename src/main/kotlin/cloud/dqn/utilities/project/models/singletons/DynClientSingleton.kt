package cloud.dqn.utilities.project.models.singletons

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB
import com.amazonaws.services.dynamodbv2.document.DynamoDB


class DynClientSingleton private constructor(initParams: InitParams? = null) {
    val client: DynamoDB

    init {
        val amazonDynamoDB: AmazonDynamoDB = DynAmazonDynamoDB.factory(initParams)
        this.client = DynamoDB(amazonDynamoDB)
    }

    companion object: SingletonHolder<DynClientSingleton, InitParams?>(::DynClientSingleton)
}