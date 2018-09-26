package de.jbamberger.algorithms.map


class MyAVLTreeTest : AVLTreeTest() {

    override fun getImplementation(): BinaryTree<Int, String> {
        return AVLTree()
    }
}
