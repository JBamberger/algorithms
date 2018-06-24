package de.jbamberger.algorithms.sort

/**
 * @author Jannik Bamberger (dev.jbamberger@gmail.com)
 */

fun <T> mergeSort(input: Array<T>, compare: Comparator<T>) {
    fun localMergeSort(l: Int, r: Int) {

    }
    localMergeSort(0, input.size - 1)
}

fun <T> quickSort(input: Array<T>, compare: Comparator<T>) {
    fun localQuickSort(l: Int, r: Int) {
        if (l >= r) return

        var i = l + 1;
        var j = r;
        val p = input[l]

        while (i <= j) {
            while (i <= j && (compare.compare(input[i], p) < 0)) i++
            while (i <= j && (compare.compare(input[j], p) >= 0)) j--
            if (i < j) input.swap(i, j)
        }
        if (l < j) {
            input.swap(l, j)
            localQuickSort(l, j-1)
        }
        if (j < r) {
            localQuickSort(j+1, r)
        }

    }
    localQuickSort(0, input.size - 1)
}

fun <T> heapSort(input: Array<T>, compare: Comparator<T>) {

}

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

