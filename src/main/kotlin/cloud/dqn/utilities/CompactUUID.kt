package cloud.dqn.utilities

import java.nio.BufferUnderflowException
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.charset.Charset
import java.util.*

class CompactUUID {
    val uuid: UUID
    val str: String
    val charset: Charset

    constructor(uuid: UUID = UUID.randomUUID(), charset: Charset = DEFAULT_CHARSET) {
        this.charset = charset
        this.uuid = uuid
        this.str = toString(uuid, charset)
    }

    private constructor(uuid: UUID, str: String, charset: Charset) {
        this.charset = charset
        this.uuid = uuid
        this.str = str
    }

    fun validateConversion(): Boolean {
        return uuid == from(str, charset)?.uuid && str == toString(uuid, charset)
    }

    companion object {
        private val ENCODER = Base64.getUrlEncoder().withoutPadding()
        private val DECODER = Base64.getUrlDecoder()
        private val DEFAULT_CHARSET = Charsets.UTF_8
        private val BYTE_ORDER = ByteOrder.BIG_ENDIAN
        private val UUID_BYTE_ARRAY_SIZE = 16
        private val DELIMITER = "="

        private fun toString(uuid: UUID, charset: Charset): String {
            val byteArrayUUID = uuidToByteArray(uuid)
            return String(ENCODER.encode(byteArrayUUID), charset).split(DELIMITER)[0]
        }

        fun from(str: String, charset: Charset = DEFAULT_CHARSET): CompactUUID? {
            val trimmed = str.trim()
            val decode = try {
                DECODER.decode(trimmed.toByteArray(charset))
            } catch (e: IllegalArgumentException) {
                null
            }
            return decode?.let { byteArray ->
                byteArrayToUUID(byteArray)?.let { uuid ->
                    CompactUUID(uuid, trimmed, charset)
                }
            }
        }

        private fun uuidToByteArray(uuid: UUID): ByteArray {
            val byteArray = ByteArray(UUID_BYTE_ARRAY_SIZE)
            val bb = ByteBuffer.wrap(byteArray)
            bb.order(BYTE_ORDER)
            bb.putLong(uuid.mostSignificantBits)
            bb.putLong(uuid.leastSignificantBits)
            return bb.array()
        }

        private fun byteArrayToUUID(byteArray: ByteArray): UUID? {
            val bb = ByteBuffer.wrap(byteArray)
            bb.order(BYTE_ORDER)
            return try {
                bb.long // a required call
                bb.long // a required call
                bb.flip()
                UUID(bb.long, bb.long)
            } catch (e: BufferUnderflowException) {
                null
            }

        }
    }
}