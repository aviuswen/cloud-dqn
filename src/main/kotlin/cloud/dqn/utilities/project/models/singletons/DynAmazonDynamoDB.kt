package cloud.dqn.utilities.project.models.singletons

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder

class DynAmazonDynamoDB {
    companion object {
        fun factory(aws: InitParams? = null): AmazonDynamoDB {
            val builder = AmazonDynamoDBClientBuilder.standard()
            aws?.awsCredentials?.let {
                builder.credentials = AWSStaticCredentialsProvider(it)
            }
            aws?.awsRegion?.let {
                builder.withRegion(it)
            }
            return builder.build()
        }
    }
}