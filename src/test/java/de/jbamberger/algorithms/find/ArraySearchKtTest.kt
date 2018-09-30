package de.jbamberger.algorithms.find

import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

/**
 * @author Jannik Bamberger (dev.jbamberger@gmail.com)
 */
@RunWith(Parameterized::class)
class ArraySearchKtTest constructor(private val algo: IntArraySearchAlgorithm) {

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun params(): List<IntArraySearchAlgorithm> {
            return listOf(
                    ::binSearch,
                    ::interpolationSearch
            )
        }

        fun assertSorted(data: Array<Int>) {
            for (i in 1 until data.size) {
                MatcherAssert.assertThat(data[i - 1], Matchers.lessThan(data[i]))
            }
        }
    }

    @Test
    fun emptyArray() {
        val empty = IntArray(0)
        assertThat(algo(empty, 5)).isEqualTo(-1)
    }

    @Test
    fun sizeOne() {
        val array = IntArray(1) { 1 }
        assertThat(algo(array, 5)).isEqualTo(-1)
        assertThat(algo(array, 1)).isEqualTo(0)
    }

    @Test
    fun evenlySpacedPositive() {
        val array = IntArray(100) { it }
        assertThat(algo(array, -5)).isEqualTo(-1)
        assertThat(algo(array, 0)).isEqualTo(0)
        assertThat(algo(array, 5)).isEqualTo(5)
        assertThat(algo(array, 99)).isEqualTo(99)
        assertThat(algo(array, 100)).isEqualTo(-1)
        assertThat(algo(array, 1000)).isEqualTo(-1)
    }

    @Test
    fun evenlySpacedMixedSign() {
        val array = IntArray(100) { it - 50 }
        assertThat(algo(array, -50)).isEqualTo(0)
        assertThat(algo(array, -5)).isEqualTo(45)
        assertThat(algo(array, 0)).isEqualTo(50)
        assertThat(algo(array, 49)).isEqualTo(99)
        assertThat(algo(array, 100)).isEqualTo(-1)
    }

    @Test
    fun hugeValues() {
        val array = IntArray(3) { when (it) {
            0 -> Int.MIN_VALUE
            1 -> 0
            2 -> Int.MAX_VALUE
            else -> throw IllegalArgumentException()
        } }
        assertThat(algo(array, Int.MIN_VALUE)).isEqualTo(0)
        assertThat(algo(array, 0)).isEqualTo(1)
        assertThat(algo(array, Int.MAX_VALUE)).isEqualTo(2)
        assertThat(algo(array, 100)).isEqualTo(-1)
        assertThat(algo(array, -100)).isEqualTo(-1)
    }
}
