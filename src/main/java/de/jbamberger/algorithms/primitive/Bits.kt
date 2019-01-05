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

    // allowed range of values for each pattern
    private val mask: Int


    private val dataLength: Int
    private val data: ByteArray

    private val getIndexInArray: Int
    private val getIndexInByte: Int

    private val masks: ByteArray

    init {
        if (length < 0) {
            throw IllegalArgumentException("length must be positive, actual value is $length")
        }


        val patternsPerByte = 8 / patternSize


        getIndexInArray = when (patternsPerByte) {
            8 -> 3
            4 -> 2
            2 -> 1
            1 -> 0
            else -> throw IllegalArgumentException("patternSize must be 1,2 or 4 but is $patternSize")
        }

        mask = (0xff ushr (8 - patternSize)) and 0xff

        getIndexInByte = (0xff xor (0xff shl getIndexInArray)) and 0xff

        // determine the number of bytes required to store the given number of patterns
        dataLength = Math.ceil(length.toDouble() / patternsPerByte.toDouble()).toInt()

        // init the backing data buffer
        data = ByteArray(dataLength)

        // initialize a bit-mask for each position in the byte
        masks = ByteArray(patternsPerByte) {
            (mask shl (it * patternSize)).toByte()
        }

    }


    operator fun set(pos: Int, value: Byte) {
        // value range check
        if (value.toInt() and mask.inv() != 0) throw IllegalArgumentException("Value $value out of range.")
        // position range check
        if (pos < 0 || length <= pos) throw IllegalArgumentException("Index $pos out of range. [0, $length)")

        val div = pos shr getIndexInArray
        val mod = pos and getIndexInByte

        val remainingData = data[div] and (masks[mod].inv())
        //val posData = data[div] and masks[mod]

        val shifted = ((value.toInt() and mask) shl (mod * patternSize)).toByte()
        val combined = remainingData xor shifted

        data[div] = combined
    }

    operator fun get(pos: Int): Byte {
        if (pos < 0 || length <= pos) throw IllegalArgumentException("Index $pos out of range. [0, $length)")

        val div = pos shr getIndexInArray
        val mod = pos and getIndexInByte

        // extended to int, such that we can shift
        val maskedData = (data[div] and masks[mod]).toInt()

        val result = maskedData ushr (mod * patternSize)

        return (result and mask).toByte()
    }

    fun getBackingData(): ByteArray = data
}