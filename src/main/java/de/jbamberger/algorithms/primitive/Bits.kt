package de.jbamberger.algorithms.primitive

import kotlin.experimental.and
import kotlin.experimental.inv
import kotlin.experimental.xor

class Bits(private val length: Int, private val patternSize: Int) {

    private val mask: Int
    private val patternsPerByte: Int
    private val dataLength: Int
    private val data: ByteArray

    private val masks: ByteArray

    init {
        if (length < 0) {
            throw IllegalArgumentException("length must be positive, actual value is $length")
        }

        when (patternSize) {
            1 -> {
                mask = 0b0000_0001
                patternsPerByte = 8
            }
            2 -> {
                mask = 0b0000_0011
                patternsPerByte = 4
            }
            4 -> {
                mask = 0b0000_1111
                patternsPerByte = 2
            }
            else -> mask = (throw IllegalArgumentException("patternSize must be 1,2 or 4 but is $patternSize"))
        }

        dataLength = Math.ceil(length.toDouble() / patternsPerByte.toDouble()).toInt()
        data = ByteArray(dataLength)


        masks = ByteArray(patternsPerByte) {
            (mask shl (it * patternSize)).toByte()
        }

    }


    operator fun set(pos: Int, value: Byte) {
        // value range check
        if (value.toInt() and mask.inv() != 0) throw IllegalArgumentException("Value $value out of range.")
        // position range check
        if (pos < 0 || length <= pos) throw IllegalArgumentException("Index $pos out of range. [0, $length)")

        val div = pos / patternsPerByte
        val mod = pos % patternsPerByte

        val remainingData = data[div] and (masks[mod].inv())
        //val posData = data[div] and masks[mod]

        val shifted = ((value.toInt() and mask) shl (mod * patternSize)).toByte()
        val combined = remainingData xor shifted

        data[div] = combined
    }

    operator fun get(pos: Int): Byte {
        if (pos < 0 || length <= pos) throw IllegalArgumentException("Index $pos out of range. [0, $length)")
        // the byte containing the pattern
        val div = pos / patternsPerByte
        // the position within the byte
        val mod = pos % patternsPerByte

        // extended to int, such that we can shift
        val maskedData = (data[div] and masks[mod]).toInt()

        val result = maskedData ushr (mod * patternSize)

        return (result and mask).toByte()
    }

    fun getBackingData(): ByteArray = data
}