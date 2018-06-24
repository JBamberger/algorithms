package de.jbamberger.algorithms.find

/**
 * @author Jannik Bamberger (dev.jbamberger@gmail.com)
 */


fun <T : Comparable<T>> binSearch(M: Array<T>, k: T): Int {
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
    var l = 0
    var r = M.size - 1
    while (k >= M[l] && k <= M[r]) {
        val m = l + ((k - M[l]) / (M[r] - M[l])) * (r - l)
        when {
            k > M[m] -> l = m + 1
            k < M[m] -> r = m - 1
            else -> return m
        }
    }
    return -1
}