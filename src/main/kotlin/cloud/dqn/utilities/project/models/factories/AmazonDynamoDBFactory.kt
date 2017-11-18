package cloud.dqn.utilities.project.models.factories

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder

object AmazonDynamoDBFactory {
    fun build(initParams: InitParams = InitParams()): AmazonDynamoDB {
        val builder = AmazonDynamoDBClientBuilder.standard()
        initParams.awsCredentials?.let {
            builder.credentials = AWSStaticCredentialsProvider(it)
        }
        builder.withRegion(initParams.awsRegion)
        return builder.build()
    }
}