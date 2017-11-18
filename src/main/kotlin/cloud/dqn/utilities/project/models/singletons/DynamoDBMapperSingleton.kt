package cloud.dqn.utilities.project.models.singletons

import cloud.dqn.utilities.project.models.factories.DynamoDBMapperFactory
import cloud.dqn.utilities.project.models.factories.InitParams
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper

class DynamoDBMapperSingleton private constructor() {

    companion object: SingletonHolder<DynamoDBMapper, InitParams>(DynamoDBMapperFactory::build)

}