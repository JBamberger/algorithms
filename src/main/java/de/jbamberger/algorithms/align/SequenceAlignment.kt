package de.jbamberger.algorithms.align

/**
 * @author Jannik Bamberger (dev.jbamberger@gmail.com)
 */

interface ArraySequenceAligner<T> {
    /**
     * This function computes the best alignment of the two sequences according to the edit distance function.
     * The result is the alignment sequence, and the score of the distance sum.
     */
    fun align(a: Array<T>, b: Array<T>, d: (T, T) -> Int, empty: T): Alignment<T>

}

data class Alignment<T>(
        val align: Collection<Pair<T, T>>,
        val distance: Int) {
    val len = align.size
}

object Distances {
    fun editDistance(c1: Char, c2: Char): Int {
        return when {
            c1 == '-' && c2 == '-' -> Int.MAX_VALUE
            c1 == c2 -> 0
            c1 != c2 -> 1
            else -> Int.MAX_VALUE
        }
    }
}