package de.jbamberger.algorithms.sort

import kotlin.math.ceil

/**
 * @author Jannik Bamberger (dev.jbamberger@gmail.com)
 */

typealias InPlaceSortingAlgorithm<T> = (Array<T>, java.util.Comparator<T>) -> Unit


fun bucketSort(input: DoubleArray, sort: (MutableList<Double>) -> MutableList<Double>) {
    val n = input.size
    val B = Array<MutableList<Double>>(n) { mutableListOf() }
    for (i in 0 until n) {
        B[ceil(n * input[i]).toInt() - 1].add(input[i])
    }
    var i = 0
    for (j in 0 until n) {
        B[j] = sort(B[j])
        for (k in 0 .. B[j].size) {
            input[i] = B[j][k]
            i++
        }
    }
}

fun <T> mergeSort(input: Array<T>, compare: Comparator<T>) {
    operator fun T.compareTo(i: T): Int {
        return compare.compare(this, i)
    }

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

fun <T> randomizedQuickSort(input: Array<T>, compare: Comparator<T>) {
    quickSort(input.shuffle(), compare)
}

fun <T> quickSort(input: Array<T>, compare: Comparator<T>) {
    fun localQuickSort(l: Int, r: Int) {
        if (l >= r) return

        var i = l + 1
        var j = r
        val p = input[l]

        while (i <= j) {
            while (i <= j && (compare.compare(input[i], p) < 0)) i++
            while (i <= j && (compare.compare(input[j], p) >= 0)) j--
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

fun <T> heapSort(input: Array<T>, compare: Comparator<T>) {
    operator fun T.compareTo(i: T): Int {
        return compare.compare(this, i)
    }

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

fun <T> selectionSort(input: Array<T>, compare: Comparator<T>) {
    val n = input.size
    for (i in 0..n - 2) {
        var m = i;
        for (j in i until n) {
            if (compare.compare(input[j], input[m]) < 0) {
                m = j
            }
        }
        input.swap(i, m)
    }
}

