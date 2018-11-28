package de.jbamberger.algorithms.primitive

import de.jbamberger.algorithms.primitive.Bits
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

    @Test
    fun size1() {
        val bits = Bits(16, 1)

        assertThat(bits[0]).isEqualTo(0)
        bits[0] = 1
        assertThat(bits[0]).isEqualTo(1)
        assertThat(bits[1]).isEqualTo(0)
        bits[1] = 1
        assertThat(bits[1]).isEqualTo(1)
        assertThat(bits[2]).isEqualTo(0)
        bits[2] = 1
        assertThat(bits[2]).isEqualTo(1)
        assertThat(bits[3]).isEqualTo(0)
        bits[3] = 1
        assertThat(bits[3]).isEqualTo(1)
        assertThat(bits[4]).isEqualTo(0)
        bits[4] = 1
        assertThat(bits[4]).isEqualTo(1)
        assertThat(bits[5]).isEqualTo(0)
        bits[5] = 1
        assertThat(bits[5]).isEqualTo(1)
        assertThat(bits[6]).isEqualTo(0)
        bits[6] = 1
        assertThat(bits[6]).isEqualTo(1)
        assertThat(bits[7]).isEqualTo(0)
        bits[7] = 1
        assertThat(bits[7]).isEqualTo(1)
        assertThat(bits[8]).isEqualTo(0)
        bits[8] = 1
        assertThat(bits[8]).isEqualTo(1)
        assertThat(bits[9]).isEqualTo(0)
        bits[9] = 1
        assertThat(bits[9]).isEqualTo(1)
        assertThat(bits[10]).isEqualTo(0)
        bits[10] = 1
        assertThat(bits[10]).isEqualTo(1)
        assertThat(bits[11]).isEqualTo(0)
        bits[11] = 1
        assertThat(bits[11]).isEqualTo(1)
        assertThat(bits[12]).isEqualTo(0)
        bits[12] = 1
        assertThat(bits[12]).isEqualTo(1)
        assertThat(bits[13]).isEqualTo(0)
        bits[13] = 1
        assertThat(bits[13]).isEqualTo(1)
        assertThat(bits[14]).isEqualTo(0)
        bits[14] = 1
        assertThat(bits[14]).isEqualTo(1)
        assertThat(bits[15]).isEqualTo(0)
        bits[15] = 1
        assertThat(bits[15]).isEqualTo(1)
    }

    @Test
    fun size2() {
        val bits = Bits(8, 2)

        assertThat(bits[0]).isEqualTo(0)
        bits[0] = 1
        assertThat(bits[0]).isEqualTo(1)
        assertThat(bits[1]).isEqualTo(0)
        bits[1] = 1
        assertThat(bits[1]).isEqualTo(1)
        assertThat(bits[2]).isEqualTo(0)
        bits[2] = 1
        assertThat(bits[2]).isEqualTo(1)
        assertThat(bits[3]).isEqualTo(0)
        bits[3] = 1
        assertThat(bits[3]).isEqualTo(1)
        assertThat(bits[4]).isEqualTo(0)
        bits[4] = 1
        assertThat(bits[4]).isEqualTo(1)
        assertThat(bits[5]).isEqualTo(0)
        bits[5] = 1
        assertThat(bits[5]).isEqualTo(1)
        assertThat(bits[6]).isEqualTo(0)
        bits[6] = 1
        assertThat(bits[6]).isEqualTo(1)
        assertThat(bits[7]).isEqualTo(0)
        bits[7] = 1
        assertThat(bits[7]).isEqualTo(1)

        bits[0] = 2
        assertThat(bits[0]).isEqualTo(2)
        bits[1] = 2
        assertThat(bits[1]).isEqualTo(2)
        bits[2] = 2
        assertThat(bits[2]).isEqualTo(2)
        bits[3] = 2
        assertThat(bits[3]).isEqualTo(2)
        bits[4] = 2
        assertThat(bits[4]).isEqualTo(2)
        bits[5] = 2
        assertThat(bits[5]).isEqualTo(2)
        bits[6] = 2
        assertThat(bits[6]).isEqualTo(2)
        bits[7] = 2
        assertThat(bits[7]).isEqualTo(2)

        bits[0] = 3
        assertThat(bits[0]).isEqualTo(3)
        bits[1] = 3
        assertThat(bits[1]).isEqualTo(3)
        bits[2] = 3
        assertThat(bits[2]).isEqualTo(3)
        bits[3] = 3
        assertThat(bits[3]).isEqualTo(3)
        bits[4] = 3
        assertThat(bits[4]).isEqualTo(3)
        bits[5] = 3
        assertThat(bits[5]).isEqualTo(3)
        bits[6] = 3
        assertThat(bits[6]).isEqualTo(3)
        bits[7] = 3
        assertThat(bits[7]).isEqualTo(3)

        bits[0] = 0
        assertThat(bits[0]).isEqualTo(0)
        bits[1] = 0
        assertThat(bits[1]).isEqualTo(0)
        bits[2] = 0
        assertThat(bits[2]).isEqualTo(0)
        bits[3] = 0
        assertThat(bits[3]).isEqualTo(0)
        bits[4] = 0
        assertThat(bits[4]).isEqualTo(0)
        bits[5] = 0
        assertThat(bits[5]).isEqualTo(0)
        bits[6] = 0
        assertThat(bits[6]).isEqualTo(0)
        bits[7] = 0
        assertThat(bits[7]).isEqualTo(0)
    }

}