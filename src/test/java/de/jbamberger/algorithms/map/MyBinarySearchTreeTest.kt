package de.jbamberger.algorithms.map


class MyBinarySearchTreeTest : BinarySearchTreeTest() {
    override fun <K : Comparable<K>, V> getImplementation(): BinaryTree<K, V> {
        return BinarySearchTree()
    }
}
