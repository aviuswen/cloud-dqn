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
 */
data class InitParams(
        val awsRegion: com.amazonaws.regions.Regions = Regions.US_WEST_1,
        val awsCredentials: BasicAWSCredentials? = null
)