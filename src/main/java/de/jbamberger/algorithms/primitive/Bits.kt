package de.jbamberger.algorithms.primitive

import kotlin.experimental.and
import kotlin.experimental.inv
import kotlin.experimental.or

class Bits(val length: Int, private val patternSize: Int) {

    private val mask: Int
    private val patternsPerByte: Int
    private val dataLength: Int
    private val data: ByteArray

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
    }


    operator fun set(pos: Int, value: Byte) {
        val div = pos / patternsPerByte
        val shiftedMask = (mask shl ((pos % patternsPerByte)) * patternSize).toByte()
        val clearpos = (data[div] and shiftedMask.inv())
        val newdat = (value and shiftedMask)

        data[div] = clearpos or newdat
    }

    operator fun get(pos: Int): Byte {
        val div = pos / patternsPerByte
        val mod = pos % patternsPerByte
        val shiftedMask = (mask shl mod).toByte()
        val unshiftedData = data[div] and shiftedMask
        val unshiftedInt = (unshiftedData.toInt() and 0xff)
        val shifted = (unshiftedInt shr mod)

        // Since we work with unsigned values, but Kotlin converts bytes to singned integers, the value must be
        // masked, such that we shift in 0 instead of 1s, if the position is close to the border.
        return shifted.toByte()
    }
}