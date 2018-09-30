package de.jbamberger.algorithms.align

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
class StringSequenceAlignerImpl : StringSequenceAligner {
    override fun align(a: String, b: String, d: (Char, Char) -> Int): StringSequenceAligner.Alignment {

        val left = 1
        val above = 2
        val diag = 4

        val n = a.length
        val m = b.length

        // distance matrix
        val dist = Array(n + 1) { IntArray(m + 1) }
        // pointer to the next best position
        val prev = Array(n + 1) { IntArray(m + 1) }

        dist[0][0] = 0
        for (j in 1..m) {
            dist[0][j] = dist[0][j - 1] + d('-', b[j - 1])
            prev[0][j] = left
        }

        for (i in 1..n) {
            dist[i][0] = dist[i - 1][0] + d(a[i - 1], '-')
            prev[i][0] = above

            for (j in 1..m) {
                dist[i][j] = dist[i - 1][j - 1] + d(a[i - 1], b[j - 1])
                prev[i][j] = diag

                if (dist[i][j - 1] + d('-', b[j - 1]) < dist[i][j]) {
                    dist[i][j] = dist[i][j - 1] + d('-', b[j - 1])
                    prev[i][j] = left
                }

                if (dist[i - 1][j] + d(a[i - 1], '-') < dist[i][j]) {
                    dist[i][j] = dist[i - 1][j] + d(a[i - 1], '-')
                    prev[i][j] = above
                }
            }
        }

        var i = n
        var j = m
        val aBuilder = StringBuilder()
        val bBuilder = StringBuilder()

        while (i + j > 0) {
            if (prev[i][j] == diag) {
                aBuilder.append(a[i - 1]); i--
                bBuilder.append(b[j - 1]); j--
            } else {
                if (prev[i][j] == left) {
                    aBuilder.append('-')
                    bBuilder.append(b[j - 1]); j--
                } else {
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