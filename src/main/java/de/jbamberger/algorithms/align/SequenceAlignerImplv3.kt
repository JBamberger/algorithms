package de.jbamberger.algorithms.align

/**
 * This implementation has no distance matrix. The next optimal step is computed dynamically when the sequences are
 * reconstructed.
 */
class SequenceAlignerImplv3<T> : ArraySequenceAligner<T> {
    override fun align(a: Array<T>, b: Array<T>, d: (T, T) -> Int, empty: T): ArraySequenceAligner.Alignment<T> {
        val n = a.size
        val m = b.size

        // distance matrix
        val dist = Array(n + 1) { IntArray(m + 1) }

        dist[0][0] = 0
        for (j in 1..m) {
            dist[0][j] = dist[0][j - 1] + d(empty, b[j - 1])
        }

        for (i in 1..n) {
            dist[i][0] = dist[i - 1][0] + d(a[i - 1], empty)

            for (j in 1..m) {
                dist[i][j] = dist[i - 1][j - 1] + d(a[i - 1], b[j - 1])

                if (dist[i][j - 1] + d(empty, b[j - 1]) < dist[i][j]) {
                    dist[i][j] = dist[i][j - 1] + d(empty, b[j - 1])
                }

                if (dist[i - 1][j] + d(a[i - 1], empty) < dist[i][j]) {
                    dist[i][j] = dist[i - 1][j] + d(a[i - 1], empty)
                }
            }
        }

        var i = n
        var j = m
        val aAlignment = MutableList(0) { empty }
        val bAlignment = MutableList(0) { empty }

        while (i + j > 0) {
            when {
                dist[i - 1][j - 1] <= dist[i][j - 1] && dist[i - 1][j - 1] <= dist[i - 1][j] -> {
                    aAlignment.add(a[i - 1]); i--
                    bAlignment.add(b[j - 1]); j--
                }
                dist[i][j - 1] <= dist[i - 1][j - 1] && dist[i][j - 1] <= dist[i - 1][j] -> {
                    aAlignment.add(empty)
                    bAlignment.add(b[j - 1]); j--
                }
                else -> {
                    aAlignment.add(a[i - 1]); i--
                    bAlignment.add(empty)
                }
            }
        }

        aAlignment.reverse()
        bAlignment.reverse()

        return ArraySequenceAligner.Alignment(aAlignment, bAlignment, dist[n][m])
    }
}