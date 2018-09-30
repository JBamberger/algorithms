package de.jbamberger.algorithms.align

/**
 * @author Jannik Bamberger (dev.jbamberger@gmail.com)
 */
interface StringSequenceAligner {
    /**
     * This function computes the best alignment of the two sequences according to the edit distance function.
     * The result is the alignment sequence, and the score of the distance sum.
     */
    fun align(a: String, b: String, d: (Char, Char) -> Int): Alignment

    data class Alignment(
            val seq1: String,
            val seq2: String,
            val distance: Int) {

        val len: Int

        init {
            if (seq1.length != seq2.length) {
                throw IllegalArgumentException("sequence lengths must be equal. Sequences where\nseq1: $seq1\nseq2: $seq2")
            }
            len = seq1.length
        }

        fun greatestCommonSubsequence(): String {
            val builder = StringBuilder()
            for (i in 0 until len) {
                val c1 = seq1[i]
                val c2 = seq2[i]
                if (c1 == c2 && c1 != '-') {
                    builder.append(c1)
                }
            }
            return builder.toString()
        }
    }
}

interface ArraySequenceAligner<T> {
    /**
     * This function computes the best alignment of the two sequences according to the edit distance function.
     * The result is the alignment sequence, and the score of the distance sum.
     */
    fun align(a: Array<T>, b: Array<T>, d: (T, T) -> Int, empty: T): Alignment<T>

    data class Alignment<T>(
            val seq1: List<T>,
            val seq2: List<T>,
            val distance: Int) {

        val len: Int

        init {
            if (seq1.size != seq2.size) {
                throw IllegalArgumentException("sequence lengths must be equal. Sequences where\nseq1: $seq1\nseq2: $seq2")
            }
            len = seq1.size
        }
    }
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