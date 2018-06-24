package de.jbamberger.algorithms.sort

/**
 * @author Jannik Bamberger (dev.jbamberger@gmail.com)
 */

fun <T> selectionSort(input: Array<T>, compare: Comparator<T>) {
    val n = input.size
    for (i in 0..n - 2) {
        var m = i;
        for (j in i until n) {
            if (compare.compare(input[j], input[m]) < 0) {
                m = j
            }
        }
        input.swap(i, m)
    }
}

