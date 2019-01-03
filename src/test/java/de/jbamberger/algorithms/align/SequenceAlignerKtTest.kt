package de.jbamberger.algorithms.align

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

/**
 * @author Jannik Bamberger (dev.jbamberger@gmail.com)
 */


@RunWith(Parameterized::class)
class SequenceAlignerKtTest constructor(private val algo: ArraySequenceAligner<Char>) {

    companion object {
        @JvmStatic
        @Parameterized.Parameters()
        fun params(): List<ArraySequenceAligner<Char>> {
            return listOf(
                    SequenceAlignerImplv1(),
                    SequenceAlignerImplv2()
            )
        }
    }

    @Test
    fun empty() {
        val alignment = algo.align(emptyArray(), emptyArray(), Distances::editDistance, '-')
        assertThat(alignment.align).isEmpty()
        assertThat(alignment.distance).isEqualTo(0)
        assertThat(alignment.len).isEqualTo(0)
    }

    @Test
    fun mediumLengthSequences() {
        val s1 = "ACCTG".toCharArray().toTypedArray()
        val s2 = "AACG".toCharArray().toTypedArray()
        val alignment = algo.align(s1, s2, Distances::editDistance, '-')

        assertThat(alignment.align).isEqualTo(s1.asList().zip("AAC-G".toCharArray().toTypedArray()))
        assertThat(alignment.distance).isEqualTo(2) // one mismatch, one substitution
        assertThat(alignment.len).isEqualTo(5)

        val alignmentReverse = algo.align(s2, s1, Distances::editDistance, '-')
        assertThat(alignment.align).isEqualTo(alignmentReverse.align.map { (a, b) -> Pair(b, a) })
        assertThat(alignment.distance).isEqualTo(alignmentReverse.distance)
    }

}