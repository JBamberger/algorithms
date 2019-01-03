package de.jbamberger.algorithms.primitive

import kotlin.experimental.and
import kotlin.experimental.inv
import kotlin.experimental.xor

/**
 * This class allows to store bit patterns. It supports only 1,2, and 4-bit values. The values are stored in a
 * primitive byte array.
 *
 * @param length the number of patterns that should be saved
 * @param patternSize number of bits per pattern
 */
class Bits(private val length: Int, private val patternSize: Int) {

    private val mask: Int
    private val patternsPerByte: Int
    private val dataLength: Int
    private val data: ByteArray

    private val pShift: Int
    private val pPat: Int

    private val masks: ByteArray

    init {
        if (length < 0) {
            throw IllegalArgumentException("length must be positive, actual value is $length")
        }

        when (patternSize) {
            1 -> {
                mask = 0b0000_0001
                patternsPerByte = 8
                pShift = 3 // log2 of patternsPerByte
                pPat = 0b0000_0111
            }
            2 -> {
                mask = 0b0000_0011
                patternsPerByte = 4
                pShift = 2
                pPat = 0b0000_0011
            }
            4 -> {
                mask = 0b0000_1111
                patternsPerByte = 2
                pShift = 1
                pPat = 0b0000_0001
            }
            else -> mask = (throw IllegalArgumentException("patternSize must be 1,2 or 4 but is $patternSize"))
        }

        // determine the number of bytes required to store the given number of patterns
        dataLength = Math.ceil(length.toDouble() / patternsPerByte.toDouble()).toInt()

        // init the backing data buffer
        data = ByteArray(dataLength)

        // initialize a bit-mask for each position
        masks = ByteArray(patternsPerByte) {
            (mask shl (it * patternSize)).toByte()
        }

    }


    operator fun set(pos: Int, value: Byte) {
        // value range check
        if (value.toInt() and mask.inv() != 0) throw IllegalArgumentException("Value $value out of range.")
        // position range check
        if (pos < 0 || length <= pos) throw IllegalArgumentException("Index $pos out of range. [0, $length)")

        val div = pos shr pShift
        val mod = pos and pPat

        val remainingData = data[div] and (masks[mod].inv())
        //val posData = data[div] and masks[mod]

        val shifted = ((value.toInt() and mask) shl (mod * patternSize)).toByte()
        val combined = remainingData xor shifted

        data[div] = combined
    }

    operator fun get(pos: Int): Byte {
        if (pos < 0 || length <= pos) throw IllegalArgumentException("Index $pos out of range. [0, $length)")
        // the byte containing the pattern
        val div = pos shr pShift
        // the position within the byte
        val mod = pos and pPat

        // extended to int, such that we can shift
        val maskedData = (data[div] and masks[mod]).toInt()

        val result = maskedData ushr (mod * patternSize)

        return (result and mask).toByte()
    }

    fun getBackingData(): ByteArray = data
}