package cloud.dqn.utilities.project.models.factories

import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.regions.Regions

/**
 * WARNING:
 *  http://docs.aws.amazon.com/sdk-for-java/v2/developer-guide/credentials.html
 *  Credentials can be pull from a chain of different places
 *
 *  Typical usage should be with awsCredentials == null as
 *      loading should be done with an AWS IAM role
 *
 * Load credentials with InitParams for your own account
 * Requires that credentials have access to
 * create/describe dynamoDB tables
 *
 * @see com.amazonaws.auth.BasicAWSCredentials
 *
 * ie:
 * val credentials = BasicAWSCredentials("accessKey", "secretKey")
 * val init =  InitParams(
 *                  awsRegion = Regions.DEFAULT_REGION,
 *                  awsCredentials = credentials
 *             )
 */
data class InitParams(
        val awsRegion: com.amazonaws.regions.Regions = Regions.US_WEST_1,
        val awsCredentials: BasicAWSCredentials? = null
)