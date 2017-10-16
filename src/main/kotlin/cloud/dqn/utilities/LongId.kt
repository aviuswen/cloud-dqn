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

    private constructor(long: Long, str: String, charset: Charset) {
        this.charset = charset
        this.str = str
        this.long = long
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

        fun from(str: String, charset: Charset = DEFAULT_CHARSET): LongId? {
            val trimmed = str.trim()
            base64toLong(trimmed, charset)?.let { long ->
                if (trimmed == longToBase64Str(long, charset)) {
                    return LongId(long, trimmed, charset)
                }
            }
            return null
        }

        fun longToBase64Str(long: Long, charset: Charset): String {
            val byteArrayLong = Longs.toByteArray(long)
            val compacted = removeLeftZeroPadding(byteArrayLong)
            return byteArrayToBase64String(compacted, charset)
        }

        fun base64toLong(str: String, charset: Charset): Long? {
            base64StringToByteArray(str, charset)?.let {
                return Longs.fromByteArray(addLeftZeroPadding(it))
            }
            return null
        }

        private fun byteArrayToBase64String(byteArray: ByteArray, charset: Charset): String {
            return String(ENCODER.encode(byteArray), charset)
        }

        private fun base64StringToByteArray(base64str: String, charset: Charset): ByteArray? {
            return try {
                DECODER.decode(base64str.toByteArray(charset))
            } catch (e: IllegalArgumentException) {
                null
            }
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