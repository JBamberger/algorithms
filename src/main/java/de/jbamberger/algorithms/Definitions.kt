package de.jbamberger.algorithms

import java.util.*

/**
 * @author Jannik Bamberger (dev.jbamberger@gmail.com)
 */

fun IntArray.swap(i: Int, j: Int) {
    val tmp = this[i]
    this[i] = this[j]
    this[j] = tmp
}

fun <T> Array<T>.swap(i: Int, j: Int) {
    val tmp = this[i]
    this[i] = this[j]
    this[j] = tmp
}

fun <T> Array<T>.shuffle(): Array<T> {
    val rng = Random()

    for (index in 0 until this.size) {
        val randomIndex = rng.nextInt(this.size)

        // Swap with the random position
        val temp = this[index]
        this[index] = this[randomIndex]
        this[randomIndex] = temp
    }
    return this
}

fun minMax(M: IntArray): Pair<Int, Int> {
    var min = Int.MAX_VALUE
    var max = Int.MIN_VALUE
    for (i in 0 until M.size) {
        if (M[i] > max) max = M[i]
        if (M[i] < min) min = M[i]
    }
    return Pair(min, max)
}

fun min(M: IntArray): Int{
    var min = Int.MAX_VALUE
    for (i in 0 until M.size) {
        if (M[i] < min) min = M[i]
    }
    return min
}

fun max(M: IntArray): Int {
    var max = Int.MIN_VALUE
    for (i in 0 until M.size) {
        if (M[i] > max) max = M[i]
    }
    return max
}