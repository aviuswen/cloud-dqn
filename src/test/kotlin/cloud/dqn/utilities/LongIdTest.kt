package cloud.dqn.utilities

import org.junit.Assert
import org.junit.Test
import java.util.*

class LongIdTest {
    @Test
    fun emptyStringTest() {
        val longId = LongId.from("")
        Assert.assertTrue(longId?.validConversion() ?: false)
    }
    @Test
    fun invalidStringTest() {
        val tooLong = "01234567890123456789"
        val longId = LongId.from(tooLong)
        Assert.assertNull(longId)
        val invalidString = "abcdefghi"
        Assert.assertNull("failed with string: $invalidString", LongId.from(invalidString))
    }

    @Test
    fun negativeLongTest() {
        val value = -123L
        Assert.assertTrue("Failed with value: $value", LongId(value).validConversion())
    }

    @Test
    fun randomTesting() {
        val rand = Random()
        repeat(
            times = 4000,
            action = {
                val long = rand.nextLong()
                val longId = LongId(long)
                Assert.assertTrue("Failed with value: $long", longId.validConversion())
            }
        )
    }
}