package de.jbamberger.algorithms.align

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

/**
 * @author Jannik Bamberger (dev.jbamberger@gmail.com)
 */


@RunWith(Parameterized::class)
class StringSequenceAlignerKtTest constructor(private val algo: StringSequenceAligner) {

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun params(): List<StringSequenceAligner> {
            return listOf(
                    StringSequenceAlignerImpl(),
                    StringSequenceAlignerImplv2()
            )
        }
    }

    @Test
    fun empty() {
        val alignment = algo.align("", "", Distances::editDistance)
        assertThat(alignment.seq1).isEmpty()
        assertThat(alignment.seq2).isEmpty()
        assertThat(alignment.distance).isEqualTo(0)
        assertThat(alignment.len).isEqualTo(0)
    }

    @Test
    fun mediumLengthSequences() {
        val s1 = "ACCTG"
        val s2 = "AACG"
        val alignment = algo.align(s1, s2, Distances::editDistance)
        println(alignment)
        assertThat(alignment.seq1).isEqualTo(s1)
        assertThat(alignment.seq2).isEqualTo("AAC-G")
        assertThat(alignment.distance).isEqualTo(2) // one mismatch, one substitution
        assertThat(alignment.len).isEqualTo(5)

        val alignmentReverse = algo.align(s2, s1, Distances::editDistance)
        assertThat(alignment.seq1).isEqualTo(alignmentReverse.seq2)
        assertThat(alignment.seq2).isEqualTo(alignmentReverse.seq1)
        assertThat(alignment.distance).isEqualTo(alignmentReverse.distance)
    }

}