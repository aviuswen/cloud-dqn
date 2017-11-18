package cloud.dqn.utilities.project.models

class Sprint: Metadata {
    var projectId: String   // Primary hash
    /**
     * Secondary Hash
     *      super.id
     */

    // LSI
    var start: Int
    var end: Int

    // GSI
    var username: String

    // Other Attributes
    var endAnalysis: Analysis = Analysis()

    constructor(projectId: String, start: Int, end: Int, username: String): super() {
        this.projectId = projectId
        this.start = start
        this.end = end
        this.username = username
    }

    companion object {
        private val MAX_ANALYSIS_LENGTH = 512
    }
}

