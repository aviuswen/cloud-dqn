package cloud.dqn.utilities

import com.google.common.primitives.Longs
import java.nio.charset.Charset
import java.util.*

class LongId {

    val long: Long
    val str: String
    val charset: Charset

    constructor(long: Long, charset: Charset = DEFAULT_CHARSET) {
        this.charset = charset
        this.long = long
        this.str = longToBase64Str(this.long, charset)
    }

    constructor(str: String, charset: Charset = DEFAULT_CHARSET) {
        this.charset = charset
        this.str = str.trim()
        this.long = base64toLong(this.str, charset)
    }

    fun validConversion(): Boolean {
        return long == base64toLong(str, charset) && str == longToBase64Str(long, charset)
    }

    companion object {
        private val DEFAULT_CHARSET = Charsets.UTF_8
        private val DEFAULT_SIZE = 8
        private val ENCODER = Base64.getUrlEncoder().withoutPadding()
        private val DECODER = Base64.getUrlDecoder()
        private val ZERO_BYTE: Byte = 0

        fun longToBase64Str(long: Long, charset: Charset): String {
            val byteArrayLong = Longs.toByteArray(long)
            val compacted = removeLeftZeroPadding(byteArrayLong)
            return byteArrayToBase64String(compacted, charset)
        }

        fun base64toLong(str: String, charset: Charset): Long {
            val decodedByteArray = base64StringToByteArray(str, charset)
            val padded = addLeftZeroPadding(decodedByteArray)
            return Longs.fromByteArray(padded)
        }

        @JvmStatic fun main(args: Array<String>) {
            var index = 0L
            val max = Long.MAX_VALUE
            while(index < max) {
                if (index % 1000L == 0L) {
                    println("$index / $max")
                }
                val longId = LongId(index)
                val strId = LongId(longId.str)
                if (!longId.validConversion()) {
                    println("\tINVALID: $index")
                }
                index++
            }
            println("hi")
        }

        private fun byteArrayToBase64String(byteArray: ByteArray, charset: Charset): String {
            return String(ENCODER.encode(byteArray), charset)
        }

        private fun base64StringToByteArray(base64str: String, charset: Charset): ByteArray {
            return DECODER.decode(base64str.toByteArray(charset))
        }

        private fun addLeftZeroPadding(byteArray: ByteArray): ByteArray {
            return if (byteArray.size >= DEFAULT_SIZE) {
                byteArray
            } else {
                val destArray = ByteArray(DEFAULT_SIZE)
                System.arraycopy(byteArray, 0, destArray, DEFAULT_SIZE - byteArray.size, byteArray.size)
                destArray
            }
        }
        private fun removeLeftZeroPadding(byteArray: ByteArray): ByteArray {
            var index = 0
            for (byte in byteArray) {
                if (byte != ZERO_BYTE) { break }
                index++
            }
            return if (index < byteArray.size) {
                byteArray.copyOfRange(index, byteArray.size)
            } else {
                ByteArray(0)
            }
        }
    }
}