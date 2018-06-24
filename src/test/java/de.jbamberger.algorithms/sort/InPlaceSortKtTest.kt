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
            return listOf(::selectionSort, ::quickSort, ::randomizedQuickSort, ::mergeSort, ::heapSort)
        }

        fun assertSorted(data: Array<Int>) {
            for (i in 1 until data.size) {
                assertThat(data[i - 1], lessThan(data[i]))
            }
        }
    }

    @Test
    fun emptyArray() {
        val empty = arrayOf<Int>()
        algo(empty, Comparator.naturalOrder())
        assertThat(empty.size, Is(0))
    }

    @Test
    fun sizeOne() {
        val data = Array(1) { 1}
        assertThat(data.size, Is(1))
        algo(data, Comparator.naturalOrder())
        assertThat(data[0], Is(1))
    }

    @Test
    fun shortInput() {
        val reversed = arrayOf(5,4,3,2,1)
        algo(reversed, Comparator.naturalOrder())
        assertSorted(reversed)
        val sortedOdd = arrayOf(1,2,3,4,5)
        algo(sortedOdd, Comparator.naturalOrder())
        assertSorted(sortedOdd)
        val sortedEven = arrayOf(1,2,3,4,5,6)
        algo(sortedEven, Comparator.naturalOrder())
        assertSorted(sortedEven)
    }

    @Test
    fun longInput() {
        val reversed = Array(5000) {5000-it}
        algo(reversed, Comparator.naturalOrder())
        assertSorted(reversed)
        val sortedOdd = Array(5001) {it+1}
        algo(sortedOdd, Comparator.naturalOrder())
        assertSorted(sortedOdd)
        val sortedEven = Array(5000) {it+1}
        algo(sortedEven, Comparator.naturalOrder())
        assertSorted(sortedEven)
    }

    @Test
    fun randomInput() {
        val randomInput = Array(5000) {5000-it}.shuffle()
        algo(randomInput, Comparator.naturalOrder())
        assertSorted(randomInput)
    }


}
