package de.jbamberger.algorithms.align

/**
 * @author Jannik Bamberger (dev.jbamberger@gmail.com)
 */
interface SequenceAligner {
    /**
     * This function computes the best alignment of the two sequences according to the edit distance function.
     * The result is the alignment sequence, and the score of the distance sum.
     */
    fun align(a: String, b: String, d: (Char, Char) -> Int): SequenceAlignment

    data class SequenceAlignment(
            val seq1: String,
            val seq2: String,
            val distance: Int) {

        val len: Int

        init {
            if (seq1.length != seq2.length) {
                throw IllegalArgumentException("sequence lengthes must be equal. Sequences where\nseq1: $seq1\nseq2: $seq2")
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
            return builder.toString();
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