package de.jbamberger.algorithms

import java.util.*

/**
 * @author Jannik Bamberger (dev.jbamberger@gmail.com)
 */

fun BooleanArray.swap(i: Int, j: Int) {
    val tmp = this[i]
    this[i] = this[j]
    this[j] = tmp
}

fun CharArray.swap(i: Int, j: Int) {
    val tmp = this[i]
    this[i] = this[j]
    this[j] = tmp
}

fun ByteArray.swap(i: Int, j: Int) {
    val tmp = this[i]
    this[i] = this[j]
    this[j] = tmp
}

fun ShortArray.swap(i: Int, j: Int) {
    val tmp = this[i]
    this[i] = this[j]
    this[j] = tmp
}

fun IntArray.swap(i: Int, j: Int) {
    val tmp = this[i]
    this[i] = this[j]
    this[j] = tmp
}

fun LongArray.swap(i: Int, j: Int) {
    val tmp = this[i]
    this[i] = this[j]
    this[j] = tmp
}

fun FloatArray.swap(i: Int, j: Int) {
    val tmp = this[i]
    this[i] = this[j]
    this[j] = tmp
}

fun DoubleArray.swap(i: Int, j: Int) {
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
        this.swap(index, rng.nextInt(this.size))
    }
    return this
}

fun minMax(M: IntArray): Pair<Int, Int> {
    if (M.isEmpty()) {
        throw IllegalArgumentException("Empty arrays are not allowed.")
    }
    var min = M[0]
    var max = M[0]
    for (i in 1 until M.size) {
        if (M[i] > max) max = M[i]
        if (M[i] < min) min = M[i]
    }
    return Pair(min, max)
}

fun min(M: IntArray): Int {
    if (M.isEmpty()) {
        throw IllegalArgumentException("Empty arrays are not allowed.")
    }
    var min = M[0]
    for (i in 1 until M.size) {
        if (M[i] < min) min = M[i]
    }
    return min
}

fun max(M: IntArray): Int {
    if (M.isEmpty()) {
        throw IllegalArgumentException("Empty arrays are not allowed.")
    }
    var max = M[0]
    for (i in 1 until M.size) {
        if (M[i] > max) max = M[i]
    }
    return max
}