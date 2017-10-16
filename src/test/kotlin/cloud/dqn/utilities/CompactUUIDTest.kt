package cloud.dqn.utilities

import org.junit.Assert
import org.junit.Test

class CompactUUIDTest {
    @Test
    fun randomUUIDGenerationTest() {
        repeat(
                times = 4000,
                action = {
                    val compactUUID = CompactUUID()
                    Assert.assertTrue("uuid: ${compactUUID.uuid}", compactUUID.validateConversion())
                }
        )
    }

    @Test
    fun invalidStringTest() {
        val fake = "bar"
        val empty = ""
        Assert.assertNull(CompactUUID.from(fake))
        Assert.assertNull(CompactUUID.from(empty))
    }

}