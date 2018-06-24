package de.jbamberger.algorithms.sort

import java.util.*

/**
 * @author Jannik Bamberger (dev.jbamberger@gmail.com)
 */


fun <T> Array<T>.swap(i: Int, j:Int) {
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