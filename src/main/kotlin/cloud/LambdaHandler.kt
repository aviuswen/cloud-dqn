package cloud

import cloud.dqn.utilities.LongId
import com.amazonaws.services.lambda.runtime.Context

/**
 * For additional AWS specific parameters:
 *   http://docs.aws.amazon.com/lambda/latest/dg/java-programming-model.html
 * Getting started:
 *   http://docs.aws.amazon.com/lambda/latest/dg/get-started-step4-optional.html
 * Deployment package help:
 *   http://docs.aws.amazon.com/lambda/latest/dg/with-s3-example-deployment-pkg.html
 * CloudWatch logging requirement
 *   http://docs.aws.amazon.com/lambda/latest/dg/java-logging.html
 */

class LambdaHandler {
    fun handler(long: Long, context: Context): String {
        val logger = context.logger
        val longId = LongId(long)
        val str = "LongId($long) = ${longId.str}"
        logger.log(str)
        return longId.str
    }
}