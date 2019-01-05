package de.jbamberger.algorithms

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class MathTest {

    @Test
    fun testLog2Int(): Unit {
        for (i in 0 .. 31) {
            assertThat((1 shl i).log2()).isEqualTo(i)
        }
    }
}