package de.jbamberger.algorithms.sort

/**
 * @author Jannik Bamberger (dev.jbamberger@gmail.com)
 */

typealias InPlaceSortingAlgorithm<T> = (Array<T>, Comparator<T>) -> Unit


fun <T> Array<T>.swap(i: Int, j:Int) {
    val tmp = this[i]
    this[i] = this[j]
    this[j] = tmp
}