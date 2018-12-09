package de.jbamberger.algorithms.sort

import de.jbamberger.algorithms.max
import de.jbamberger.algorithms.minMax
import de.jbamberger.algorithms.shuffle
import de.jbamberger.algorithms.swap
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.log

/**
 * @author Jannik Bamberger (dev.jbamberger@gmail.com)
 */

typealias InPlaceSortingAlgorithm<T> = (Array<T>, java.util.Comparator<T>) -> Unit

/**
 * best case:    n log n
 * worst case:   n
 * average case: n
 * stable: yes
 * additional memory: O(n)
 * restrictions: Double values from 0 to 1
 * Create n buckets and then map the values in the buckets. Each bucket is sorted and then the buckets are written one
 * after another.
 */
fun bucketSort(input: DoubleArray, sort: (MutableList<Double>) -> MutableList<Double>) {
    val n = input.size
    val B = Array<MutableList<Double>>(n) { mutableListOf() }
    for (i in 0 until n) {
        B[ceil(n * input[i]).toInt() - 1].add(input[i])
    }
    var i = 0
    for (j in 0 until n) {
        B[j] = sort(B[j])
        for (k in 0..B[j].size) {
            input[i] = B[j][k]
            i++
        }
    }
}

/**
 * best case:    n + k
 * worst case:   n + k
 * average case: n + k
 * stable: yes
 * additional memory: O(n + k)
 * restrictions: Integer numbers with a range of k (may not be larger than Int.MAX_VALUE)
 * Creates k buckets and counts the number of occurrences of each element. Then the numbers are moved into the correct
 * places by looking them up in the array in reverse order.
 */
fun countingSort(M: IntArray) {
    val (min, max) = minMax(M)
    val k = max - min
    val n = M.size
    val C = IntArray(k)
    val Mx = IntArray(n)
    for (i in 0 until n) C[M[i] + min] = C[M[i] + min] + 1
    for (j in 1 until k) C[j] = C[j - 1] + C[j]
    for (i in n..1) {
        Mx[C[M[i] + min]] = M[i]
        C[M[i] + min] = C[M[i] + min] - 1
    }
    for (i in 0 until n) M[i] = Mx[i]
}

/**
 * Variant of countingSort, which assumes a [0,k] as the input range.
 */
inline fun <reified T : IntKeyedVal> countingSort(M: Array<T>, k: Int) {
    val Mx = Array<T?>(M.size) { null }
    countingSort(M, Mx, k)
}

fun <T : IntKeyedVal> countingSort(M: Array<T>, Mx: Array<T?>, k: Int) {
    val n = M.size
    val C = IntArray(k)
    for (i in 0 until n) C[M[i].intValue()] = C[M[i].intValue()] + 1
    for (j in 1 until k) C[j] = C[j - 1] + C[j]
    for (i in n..1) {
        Mx[C[M[i].intValue()]] = M[i]
        C[M[i].intValue()] = C[M[i].intValue()] - 1
    }
    for (i in 0 until n) M[i] = Mx[i]!!
}

interface IntKeyedVal {
    fun intValue(): Int
}

/**
 * best case:    s * (n + d)
 * worst case:   s * (n + d)
 * average case: s * (n + d)
 * stable: yes
 * additional memory: O(n+d)
 * restrictions: words of length s with radix d
 * Sort according to the digits with a different sorting scheme -> lexicographical order
 */
fun radixSort(M: IntArray, base: Int) {
    val max = max(M)
    val s = floor(log(max.toDouble(), base.toDouble())).toInt()
    for (i in 0..s) {
        TODO("Sort w.r.t. i-th digit (stable)")
    }
}

/**
 * best case:    s * n
 * worst case:   s * n
 * average case: s * n
 * stable: no
 * additional memory: O(s)
 * restrictions: Binary numbers of length s (32 bits in this case)
 * Sorts the numbers bit-wise similar to quickSort.
 */
fun radixExchangeSort(M: IntArray) {
    fun localRESort(l: Int, r: Int, b: Int) {
        var i = l
        var j = r
        while (i <= j) {
            while (i <= j && (0 == (M[i] and (1 shl b)))) i++
            while (i <= j && (1 == (M[i] and (1 shl b)))) j--
            if (i < j) M.swap(i, j)
        }

        if (b > 0) {
            localRESort(l, i - 1, b - 1)
            localRESort(i, r, b - 1)
        }
    }
    localRESort(0, M.size - 1, 31)
}

/**
 * best case:    n log n
 * worst case:   n log n
 * average case: n log n
 * stable: no
 * additional memory: O(n)
 * Split the array in the middle. Sort both parts recursively. After the recursion, sorted parts are merged.
 */
fun <T> mergeSort(input: Array<T>, compare: Comparator<T>) {
    operator fun T.compareTo(i: T) = compare.compare(this, i)
    val tmp = input.copyOf()

    fun localMergeSort(l: Int, r: Int) {
        if (l >= r) return
        val m = (l + r - 1) / 2
        localMergeSort(l, m)
        localMergeSort(m + 1, r)
        var i = l
        var j = m + 1
        var k = l
        while (i <= m && j <= r) {
            if (input[i] < input[j]) {
                tmp[k] = input[i]
                i++
            } else {
                tmp[k] = input[j]
                j++
            }
            k++
        }
        for (h in i..m) input[k + (h - i)] = input[h]
        for (h in l until k) input[h] = tmp[h]
    }
    localMergeSort(0, input.size - 1)
}

/**
 * best case:    n log n
 * worst case:   n^2
 * average case: n log n
 * stable: no
 * additional memory: O(n)
 * Shuffle the input, then do quickSort. This helps for already sorted / partly sorted lists to avoid the worst case.
 * @see quickSort
 */
fun <T> randomizedQuickSort(input: Array<T>, compare: Comparator<T>) {
    quickSort(input.shuffle(), compare)
}

/**
 * best case:    n log n
 * worst case:   n^2
 * average case: n log n
 * stable: no
 * additional memory: O(n)
 * Select a pivot element, put larger ones to the one side and smaller ones to the other side. Then go into recursion
 * for both sides.
 */
fun <T> quickSort(input: Array<T>, compare: Comparator<T>) {
    operator fun T.compareTo(i: T) = compare.compare(this, i)

    fun localQuickSort(l: Int, r: Int) {
        if (l >= r) return

        var i = l + 1
        var j = r
        val p = input[l]

        while (i <= j) {
            while (i <= j && input[i] < p) i++
            while (i <= j && input[j] >= p) j--
            if (i < j) input.swap(i, j)
        }
        if (l < j) {
            input.swap(l, j)
            localQuickSort(l, j - 1)
        }
        if (j < r) {
            localQuickSort(j + 1, r)
        }

    }
    localQuickSort(0, input.size - 1)
}

/**
 * best case:    n
 * worst case:   n log n
 * average case: n log n
 * stable: no
 * additional memory: O(1)
 * Build a heap in place, then take the uppermost element, swap it to the end and restore the heap properties. Then
 * repeat until all elements are sorted.
 */
fun <T> heapSort(input: Array<T>, compare: Comparator<T>) {
    operator fun T.compareTo(i: T) = compare.compare(this, i)

    fun heapify(x: Int, r: Int) {
        var i = x
        val a = input[i]
        var j = 2 * i + 1

        while (j <= r) {
            if (j < r && input[j + 1] > input[j]) {
                j++
            }
            if (a < input[j]) {
                input[i] = input[j]
                i = j
                j = 2 * i + 1
            } else {
                break
            }
        }
        input[i] = a
    }

    for (i in input.size / 2 - 1 downTo 0) {
        heapify(i, input.size - 1)
    }
    for (i in input.size - 1 downTo 1) {
        input.swap(i, 0)
        heapify(0, i - 1)
    }
}

/**
 * best case:    n^2
 * worst case:   n^2
 * average case: n^2
 * stable: no
 * additional memory: O(1)
 * For each position, search through the remaining positions and select the appropriate item that comes next.
 */
fun <T> selectionSort(input: Array<T>, compare: Comparator<T>) {
    operator fun T.compareTo(i: T) = compare.compare(this, i)

    val n = input.size
    for (i in 0..n - 2) {
        var m = i;
        for (j in i until n) if (input[j] < input[m]) m = j
        input.swap(i, m)
    }
}

fun <T> insertionSort(elements: Array<T>, compare: Comparator<T>) {
    operator fun T.compareTo(i: T) = compare.compare(this, i)

    for (i in 1 until elements.size) {
        val tmp = elements[i]
        var j = i - 1
        while (j >= 0 && elements[j] > tmp) {
            elements[j + 1] = elements[j]
            j--
        }
        elements[j + 1] = tmp
    }
}

