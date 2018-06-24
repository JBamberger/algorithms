package de.jbamberger.algorithms.sort

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.lessThan
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import java.util.*
import org.hamcrest.CoreMatchers.`is` as Is

/**
 * @author Jannik Bamberger (dev.jbamberger@gmail.com)
 */

@RunWith(Parameterized::class)
class SortKtTest constructor(val algo: InPlaceSortingAlgorithm<Int>) {

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun params(): List<InPlaceSortingAlgorithm<Int>> {
            return listOf(::selectionSort)
        }
    }


    @Test
    fun emptyArray() {
        val empty = Array(0) { -1 }
        assertThat(empty.size, Is(0))
        algo(empty, Comparator.naturalOrder())
        assertThat(empty.size, Is(0))
    }

    @Test
    fun reverseShort() {
        val data = Array(5) { 5 - it }
        assertThat(data.size, Is(5))
        assertThat(data[0], Is(5))
        assertThat(data[4], Is(1))
        algo(data, Comparator.naturalOrder())
        for (i in 1..4) {
            assertThat(data[i - 1], lessThan(data[i]))
        }
    }

    @Test
    fun inOrderShort() {
        val data = Array(5) { it+1 }
        assertThat(data.size, Is(5))
        assertThat(data[0], Is(1))
        assertThat(data[4], Is(5))
        algo(data, Comparator.naturalOrder())
        for (i in 1..4) {
            assertThat(data[i - 1], lessThan(data[i]))
        }
    }

    @Test
    fun sizeOne() {
        val data = Array(1) { 1}
        assertThat(data.size, Is(1))
        algo(data, Comparator.naturalOrder())
        assertThat(data[0], Is(1))
    }

}
