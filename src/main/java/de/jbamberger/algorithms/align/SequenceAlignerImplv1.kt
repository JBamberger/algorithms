package de.jbamberger.algorithms.align

import de.jbamberger.algorithms.primitive.Bits

/**
 * Basic implementation of a sequence aligner which uses a distance matrix and a prev pointer matrix.
 *
 * The distance matrix contains the best achievable distance for the subsequences until the current index.
 * The first index refers to the first sequence and the second index to the second sequence.
 *
 * The prev pointer is either left, above or diagonal, i.e. it points to the next position which is optimal.
 *
 * The algorithm consists of two steps. First, the distance and prev matrix is created. Secondly the prev matrix is
 * traversed and the matching sequences are reconstructed.
 *
 */
class SequenceAlignerImplv1<T> : ArraySequenceAligner<T> {

    companion object {
        const val LEFT: Byte = 0x01
        const val TOP: Byte = 0x02
        const val DIAG: Byte = 0x03
    }

    override fun align(a: Array<T>, b: Array<T>, d: (T, T) -> Int, empty: T): Alignment<T> {
        val n = a.size
        val m = b.size

        // distance matrix
        val dist = Array(n + 1) { IntArray(m + 1) }
        // pointer to the next best position
        val prev = Array(n + 1) { Bits(m + 1, 2) }

        dist[0][0] = 0
        for (j in 1..m) {
            dist[0][j] = dist[0][j - 1] + d(empty, b[j - 1])
            prev[0][j] = LEFT
        }

        for (i in 1..n) {
            dist[i][0] = dist[i - 1][0] + d(a[i - 1], empty)
            prev[i][0] = TOP

            for (j in 1..m) {
                dist[i][j] = dist[i - 1][j - 1] + d(a[i - 1], b[j - 1])
                prev[i][j] = DIAG

                if (dist[i][j - 1] + d(empty, b[j - 1]) < dist[i][j]) {
                    dist[i][j] = dist[i][j - 1] + d(empty, b[j - 1])
                    prev[i][j] = LEFT
                }

                if (dist[i - 1][j] + d(a[i - 1], empty) < dist[i][j]) {
                    dist[i][j] = dist[i - 1][j] + d(a[i - 1], empty)
                    prev[i][j] = TOP
                }
            }
        }

        var i = n
        var j = m

        val align = MutableList(0) { Pair(empty, empty) }

        while (i + j > 0) {
            if (prev[i][j] == DIAG) {
                align.add(Pair(a[i - 1], b[j - 1]))
                i--; j--
            } else if (prev[i][j] == LEFT) {
                align.add(Pair(empty, b[j - 1]))
                j--
            } else {
                align.add(Pair(a[i - 1], empty))
                i--
            }
        }

        return Alignment(align.reversed(), dist[n][m])

    }
}