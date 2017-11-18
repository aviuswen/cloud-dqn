package cloud.dqn.utilities.project.models.singletons

import cloud.dqn.utilities.project.models.factories.AmazonDynamoDBFactory
import cloud.dqn.utilities.project.models.factories.DynamoDBMapperFactory
import cloud.dqn.utilities.project.models.factories.InitParams
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper

/**
 * Example of Multi Attribute, singleton with optional argument
 *
 * To get singleton instance
 *
 * val someInitParams = InitParams(...)
 * ArgsSingleton.getInstance(someInitParams)
 */

class ArgsSingleton private constructor(initParams: InitParams = InitParams()) {
    val attribute0: DynamoDBMapper
    val attribute1: AmazonDynamoDB

    init {
        this.attribute0 = DynamoDBMapperFactory.build(initParams)
        this.attribute1 = AmazonDynamoDBFactory.build(initParams)
    }

    companion object: SingletonHolder<ArgsSingleton, InitParams>(::ArgsSingleton)

}