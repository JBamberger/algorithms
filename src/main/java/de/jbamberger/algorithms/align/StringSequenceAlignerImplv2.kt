package de.jbamberger.algorithms.align

/**
 * This implementation has no distance matrix. The next optimal step is computed dynamically when the sequences are
 * reconstructed.
 */
class StringSequenceAlignerImplv2 : StringSequenceAligner {
    override fun align(a: String, b: String, d: (Char, Char) -> Int): StringSequenceAligner.Alignment {
        val n = a.length
        val m = b.length

        // distance matrix
        val dist = Array(n + 1) { IntArray(m + 1) }

        dist[0][0] = 0
        for (j in 1..m) {
            dist[0][j] = dist[0][j - 1] + d('-', b[j - 1])
        }

        for (i in 1..n) {
            dist[i][0] = dist[i - 1][0] + d(a[i - 1], '-')

            for (j in 1..m) {
                dist[i][j] = dist[i - 1][j - 1] + d(a[i - 1], b[j - 1])

                if (dist[i][j - 1] + d('-', b[j - 1]) < dist[i][j]) {
                    dist[i][j] = dist[i][j - 1] + d('-', b[j - 1])
                }

                if (dist[i - 1][j] + d(a[i - 1], '-') < dist[i][j]) {
                    dist[i][j] = dist[i - 1][j] + d(a[i - 1], '-')
                }
            }
        }

        var i = n
        var j = m
        val aBuilder = StringBuilder()
        val bBuilder = StringBuilder()

        while (i + j > 0) {
            when {
                dist[i - 1][j - 1] <= dist[i][j - 1] && dist[i - 1][j - 1] <= dist[i - 1][j] -> {
                    aBuilder.append(a[i - 1]); i--
                    bBuilder.append(b[j - 1]); j--
                }
                dist[i][j - 1] <= dist[i - 1][j - 1] && dist[i][j - 1] <= dist[i - 1][j] -> {
                    aBuilder.append('-')
                    bBuilder.append(b[j - 1]); j--
                }
                else -> {
                    aBuilder.append(a[i - 1]); i--
                    bBuilder.append('-')
                }
            }
        }

        return StringSequenceAligner.Alignment(
                aBuilder.toString().reversed(),
                bBuilder.toString().reversed(),
                dist[n][m])
    }
}