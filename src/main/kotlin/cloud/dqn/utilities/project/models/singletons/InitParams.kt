package cloud.dqn.utilities.project.models.singletons

import com.amazonaws.auth.BasicAWSCredentials

data class InitParams(
    val awsCredentials: BasicAWSCredentials? = null,
    val awsRegion: com.amazonaws.regions.Regions? = null
)