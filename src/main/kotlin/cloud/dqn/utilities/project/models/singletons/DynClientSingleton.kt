package cloud.dqn.utilities.project.models.singletons

import cloud.dqn.utilities.project.models.factories.DynamoDBFactory
import cloud.dqn.utilities.project.models.factories.InitParams
import com.amazonaws.services.dynamodbv2.document.DynamoDB

class DynClientSingleton private constructor(initParams: InitParams = InitParams()) {
    private val client: DynamoDB

    init {
        this.client = DynamoDBFactory.build(initParams)
    }

    companion object: SingletonHolder<DynClientSingleton, InitParams>(::DynClientSingleton)
}