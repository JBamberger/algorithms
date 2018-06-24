package de.jbamberger.algorithms

import de.jbamberger.algorithms.sort.mergeSort

/**
 * @author Jannik Bamberger (dev.jbamberger@gmail.com)
 */


fun main(args: Array<String>) {
    print("Hello, world!")

    mergeSort(arrayOf(5,4,3,2,1), naturalOrder())
}