package cloud.dqn.utilities.project.models.mappers

import cloud.dqn.utilities.project.models.DynTable
import cloud.dqn.utilities.project.models.factories.AmazonDynamoDBFactory
import cloud.dqn.utilities.project.models.factories.InitParams
import com.amazonaws.regions.Regions
import com.amazonaws.services.dynamodbv2.model.GlobalSecondaryIndex
import com.amazonaws.services.dynamodbv2.model.Projection
import com.amazonaws.services.dynamodbv2.model.ProjectionType
import org.junit.Assert
import org.junit.Test

/**
 * Using aws credentials from aws config file
 * @see cloud.dqn.utilities.project.models.factories.InitParams
 */
class ProjectModelTest {
    @Test fun createTableTest() {
        val init = InitParams(awsRegion = Regions.US_WEST_1)
        val createRequest = ProjectModel.createTableRequest(
                AmazonDynamoDBFactory.build(init)
        )
        Assert.assertTrue(
                createRequest.tableName == ProjectModel.DYN_CONST.TABLE_NAME
        )
        Assert.assertTrue(createRequest.keySchema == ProjectModel.DYN_CONST.KEY_SCHEMA)
        /**
         * CHECK GSIS
         */
        val gsiCount = createRequest.globalSecondaryIndexes.size
        Assert.assertTrue("Has $gsiCount gsi; ProjectModel may have been updated", gsiCount == 2)
        var gsiUpdate: GlobalSecondaryIndex? = null
        var gsiCreate: GlobalSecondaryIndex? = null
        createRequest.globalSecondaryIndexes.forEach {
            when (it.indexName) {
                ProjectModel.DYN_CONST.GSI_UPDATED -> gsiUpdate = it
                ProjectModel.DYN_CONST.GSI_CREATED -> gsiCreate = it
            }
        }
        Assert.assertNotNull(gsiUpdate)
        Assert.assertNotNull(gsiCreate)

        val allProjection = Projection().withProjectionType(ProjectionType.ALL)

        Assert.assertNotNull(gsiUpdate?.provisionedThroughput)
        Assert.assertTrue(gsiUpdate?.projection == allProjection)
        Assert.assertTrue(gsiUpdate?.keySchema?.containsAll(
                ProjectModel.DYN_CONST.GSI_UPDATED_KEY_SCHEMA
        ) ?: false)

        Assert.assertNotNull(gsiCreate?.provisionedThroughput)
        Assert.assertTrue(gsiCreate?.projection == allProjection)
        Assert.assertTrue(gsiCreate?.keySchema?.containsAll(
                ProjectModel.DYN_CONST.GSI_CREATED_KEY_SCHEMA
        ) ?: false)

        /**
         * CREATE (may fail as table already exists)
         */
        val tableCreate = ProjectModel.createTable(init)
        if (tableCreate.failure()) {
            Assert.assertTrue(tableCreate.exception?.message?.contains("Table already exists") ?: false)
        }
        val tableDescription = DynTable(AmazonDynamoDBFactory.build(init))
                .getDescription(ProjectModel.DYN_CONST.TABLE_NAME)
        Assert.assertTrue(tableDescription.success())
        println("hi")
    }
}