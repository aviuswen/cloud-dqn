package cloud.dqn.utilities.project.models

class Daily: Metadata {
    var sprintId: String        // Primary hash
    /**
     * Secondary Hash
     *      super.id
     */

    // LSI
    var date: Int

    // GSI
    var username: String
    var projectId: String

    // Other Attributes
    var timeStart: Int = 0
    var timeEnd: Int = 0
    var endAnalysis: Analysis = Analysis()

    constructor(sprintId: String, date: Int, username: String, projectId: String): super() {
        this.sprintId = sprintId
        this.date = date
        this.username = username
        this.projectId = projectId
    }

    companion object {
        private val MAX_ANALYSIS_LENGTH = 512
    }
}