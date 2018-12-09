package de.jbamberger.algorithms.primitive

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

/**
 * @author Jannik Bamberger (dev.jbamberger@gmail.com)
 */
class BitsKtTest {

    @Test
    fun empty() {


    }

    @Test
    fun exceedIndex() {


    }

    fun test_size_1(bits: Bits, bitPos: Int, bytePos: Int, pattern: Byte) {
        bits[bitPos] = 1
        assertThat(bits.getBackingData()[bytePos]).isEqualTo(pattern)
        bits[bitPos] = 0
        assertThat(bits.getBackingData()[bytePos]).isEqualTo(0b0000_0000)
    }

    fun test_size_2(bits: Bits, bitPos: Int, bytePos: Int, p1:Byte, p2:Byte, p3:Byte) {
        bits[bitPos] = 1
        assertThat(bits.getBackingData()[bytePos]).isEqualTo(p1)
        bits[bitPos] = 2
        assertThat(bits.getBackingData()[bytePos]).isEqualTo(p2)
        bits[bitPos] = 3
        assertThat(bits.getBackingData()[bytePos]).isEqualTo(p3)
        bits[bitPos] = 0
        assertThat(bits.getBackingData()[bytePos]).isEqualTo(0b0000_0000)
    }

    fun test_size_4(bits: Bits, bitPos: Int, bytePos: Int) {

        for (i in 0 until 16) {
            bits[bitPos] = i.toByte()
            assertThat(bits.getBackingData()[bytePos]).isEqualTo((i shl ((bitPos - bytePos * 2) * 4)).toByte())
        }

        bits[bitPos] = 0
        assertThat(bits.getBackingData()[bytePos]).isEqualTo(0b0000_0000)
    }

    @Test
    fun size1() {
        val bits = Bits(16, 1)

        assertThat(bits.getBackingData()[0]).isEqualTo(0b0000_0000)
        assertThat(bits.getBackingData().size).isEqualTo(2)
        test_size_1(bits, 0x0, 0, 0b0000_0001.toByte())
        test_size_1(bits, 0x1, 0, 0b0000_0010.toByte())
        test_size_1(bits, 0x2, 0, 0b0000_0100.toByte())
        test_size_1(bits, 0x3, 0, 0b0000_1000.toByte())
        test_size_1(bits, 0x4, 0, 0b0001_0000.toByte())
        test_size_1(bits, 0x5, 0, 0b0010_0000.toByte())
        test_size_1(bits, 0x6, 0, 0b0100_0000.toByte())
        test_size_1(bits, 0x7, 0, 0b1000_0000.toByte())

        test_size_1(bits, 0x8, 1, 0b0000_0001.toByte())
        test_size_1(bits, 0x9, 1, 0b0000_0010.toByte())
        test_size_1(bits, 0xa, 1, 0b0000_0100.toByte())
        test_size_1(bits, 0xb, 1, 0b0000_1000.toByte())
        test_size_1(bits, 0xc, 1, 0b0001_0000.toByte())
        test_size_1(bits, 0xd, 1, 0b0010_0000.toByte())
        test_size_1(bits, 0xe, 1, 0b0100_0000.toByte())
        test_size_1(bits, 0xf, 1, 0b1000_0000.toByte())
    }

    @Test
    fun size2() {
        val bits = Bits(8, 2)

        assertThat(bits.getBackingData()[0]).isEqualTo(0b0000_0000)
        assertThat(bits.getBackingData().size).isEqualTo(2)
        test_size_2(bits, 0x0, 0, 0b0000_0001.toByte(), 0b0000_0010.toByte(), 0b0000_0011.toByte())
        test_size_2(bits, 0x1, 0, 0b0000_0100.toByte(), 0b0000_1000.toByte(), 0b0000_1100.toByte())
        test_size_2(bits, 0x2, 0, 0b0001_0000.toByte(), 0b0010_0000.toByte(), 0b0011_0000.toByte())
        test_size_2(bits, 0x3, 0, 0b0100_0000.toByte(), 0b1000_0000.toByte(), 0b1100_0000.toByte())
        test_size_2(bits, 0x4, 1, 0b0000_0001.toByte(), 0b0000_0010.toByte(), 0b0000_0011.toByte())
        test_size_2(bits, 0x5, 1, 0b0000_0100.toByte(), 0b0000_1000.toByte(), 0b0000_1100.toByte())
        test_size_2(bits, 0x6, 1, 0b0001_0000.toByte(), 0b0010_0000.toByte(), 0b0011_0000.toByte())
        test_size_2(bits, 0x7, 1, 0b0100_0000.toByte(), 0b1000_0000.toByte(), 0b1100_0000.toByte())
    }

    @Test
    fun size4() {
        val bits = Bits(4, 4)

        assertThat(bits.getBackingData()[0]).isEqualTo(0b0000_0000)
        assertThat(bits.getBackingData().size).isEqualTo(2)
        test_size_4(bits, 0x0, 0)
        test_size_4(bits, 0x1, 0)
        test_size_4(bits, 0x2, 1)
        test_size_4(bits, 0x3, 1)
    }

}