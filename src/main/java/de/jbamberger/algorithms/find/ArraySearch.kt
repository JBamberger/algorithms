package de.jbamberger.algorithms.find

/**
 * @author Jannik Bamberger (dev.jbamberger@gmail.com)
 */

typealias ArraySearchAlgorithm<T> = (Array<T>, T, java.util.Comparator<T>) -> Int

typealias IntArraySearchAlgorithm = (IntArray, Int) -> Int

fun <T : Comparable<T>> binSearch(M: Array<T>, k: T): Int {
    if (M.isEmpty()) return -1
    var l = 0
    var r = M.size - 1
    while (k >= M[l] && k <= M[r]) {
        val m = (l + r) / 2
        when {
            k > M[m] -> l = m + 1
            k < M[m] -> r = m - 1
            else -> return m
        }
    }
    return -1
}

fun <T> binSearch(M: Array<T>, k: T, cmp: Comparator<T>): Int {
    operator fun T.compareTo(x: T) = cmp.compare(this, x)
    if (M.isEmpty()) return -1
    var l = 0
    var r = M.size - 1
    while (k >= M[l] && k <= M[r]) {
        val m = (l + r) / 2
        when {
            k > M[m] -> l = m + 1
            k < M[m] -> r = m - 1
            else -> return m
        }
    }
    return -1
}

fun binSearch(M: IntArray, k: Int): Int {
    if (M.isEmpty()) return -1
    var l = 0
    var r = M.size - 1
    while (k >= M[l] && k <= M[r]) {
        val m = (l + r) / 2
        when {
            k > M[m] -> l = m + 1
            k < M[m] -> r = m - 1
            else -> return m
        }
    }
    return -1
}

fun interpolationSearch(M: IntArray, k: Int): Int {
    if (M.isEmpty()) return -1
    if (M.size == 1) return if (M[0] == k) 0 else -1

    var l = 0
    var r = M.size - 1
    while (k >= M[l] && k <= M[r]) {
        // make computations as long, since ints would overflow for some values
        val longM = l.toLong() + ((k.toLong() - M[l].toLong()) / (M[r].toLong() - M[l].toLong())) * (r - l).toLong()

        if (longM > Int.MAX_VALUE.toLong() || longM < Int.MIN_VALUE.toLong()) {
            throw ArithmeticException("Computation failed, computed index exceeds int capacity. ${Int.MIN_VALUE} <= index <= ${Int.MAX_VALUE} but index=$longM")
        }

        val m: Int = longM.toInt()
        when {
            k > M[m] -> l = m + 1
            k < M[m] -> r = m - 1
            else -> return m
        }
    }
    return -1
}