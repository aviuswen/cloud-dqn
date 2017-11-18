package cloud.dqn.utilities.project.models

import org.junit.Test

class DynIdGeneratorTest {
    /**
     * Required that credentials be set up beforehand
     * @see cloud.dqn.utilities.project.models.factories.InitParams
     */

    @Test
    fun companionCreateTableTest() {
        // val createStatus = DynIdGenerator.createTable(InitParams())
        println("hi")
    }

    @Test
    fun factoryTest() {
        val id = "project"
//        val response = DynIdGenerator.build(id)
        println("hi")
    }

    @Test
    fun getTableTest() {
        val s = DynIdGenerator.getTableDescription()
        s.exception?.printStackTrace()
        println("hi")
    }
}