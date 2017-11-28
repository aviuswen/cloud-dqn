package cloud.dqn.utilities.project.models

import cloud.dqn.utilities.project.models.mappers.ProjectModel
import cloud.dqn.utilities.project.models.response.DataResponse
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper
import com.amazonaws.services.dynamodbv2.document.DynamoDB

class Project {

    data class CreateObj (
            val user: String,
            val name: String,
            val desc: String,
            val tags: Set<String>?
    )

    /**
     * TODO ADD SOFT LIMITS
     */
    fun create(
            createObj: CreateObj,
            amz: AmazonDynamoDB
    ): DataResponse<ProjectModel?> {
        val newId = DynIdGenerator.build(ProjectModel.DYN_CONST.TABLE_NAME, DynamoDB(amz))
        return if (newId.failure()) {
            DataResponse(error = "Id generation failure: $newId")
        } else {
            val project = ProjectModel()
            project.user = createObj.user
            project.dynId(newId.data?.str)
            project.dynName(createObj.name)
            project.dynDesc(createObj.desc)
            project.dynCreated((System.currentTimeMillis() / 1000L).toInt())
            project.dynUpdated(project.dynCreated())
            project.dynTags(createObj.tags)

            val response = DataResponse(data = project)

            // // Add save expression to ensure uniqueness??
            // val mapper = DynamoDBMapper(amz)
            // val saveExpression = DynamoDBSaveExpression()
            // mapper.save(project, saveExpression)

            try {
                DynamoDBMapper(amz).save(project)
            } catch (e: Exception) {
                response.exception = e
            }
            response
        }
    }

    fun delete(
            user: String,
            id: String,
            amz: AmazonDynamoDB
    ) {
        val project = ProjectModel()
        project.dynId(id)
        project.user = user
        DynamoDBMapper(amz).delete(project)
    }

    fun get(
            user: String,
            id: String,
            amz: AmazonDynamoDB
    ): ProjectModel {
        val project = ProjectModel()
        project.user = user
        project.dynId(id)
        return DynamoDBMapper(amz).load(project)
    }
}