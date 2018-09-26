package de.jbamberger.algorithms.map

class AVLTree<K : Comparable<K>, V> : BinarySearchTree<K, V>() {

    /**
     * Do a left rotation at node u and return the new subtree root.
     * @param u starting point
     * @return new subtree root
     */
    private fun rotateLeft(u: BinarySearchTree.NodeImpl<K, V>): BinarySearchTree.NodeImpl<K, V> {
        val v = u.rightChild
        v!!.parent = u.parent
        if (u.parent != null) { // check if u is left or right child or node
            if (u.parent!!.leftChild != null && u.parent!!.leftChild!!.key === u.key) {
                u.parent!!.leftChild = v
            } else {
                u.parent!!.rightChild = v
            }
        } else {
            root = v
        }
        u.rightChild = v.leftChild
        if (u.rightChild != null) {
            u.rightChild!!.parent = u
        }
        v.leftChild = u
        u.parent = v
        updateLocalHeight(u)
        updateLocalHeight(v)
        return v
    }

    /**
     * Do a right rotation at node u and return the new subtree root.
     * @param u starting point
     * @return new subtree root
     */
    private fun rotateRight(u: BinarySearchTree.NodeImpl<K, V>): BinarySearchTree.NodeImpl<K, V> {
        val v = u.leftChild
        v!!.parent = u.parent
        if (u.parent != null) { // check if u is left or right child or node
            if (u.parent!!.leftChild != null && u.parent!!.leftChild!!.key === u.key) {
                u.parent!!.leftChild = v
            } else {
                u.parent!!.rightChild = v
            }
        } else {
            root = v
        }
        u.leftChild = v.rightChild
        if (u.leftChild != null) {
            u.leftChild!!.parent = (u)
        }
        v.rightChild = u
        u.parent = v
        updateLocalHeight(u)
        updateLocalHeight(v)
        return v
    }

    /**
     * Do the appropriate rotations at node u.
     * @param u starting point
     */
    private fun restoreBalance(u: BinarySearchTree.NodeImpl<K, V>) {
        val balance = getBalance(u)
        if (balance == -2) {
            if (u.leftChild != null && getBalance(u.leftChild!!) <= 0) {
                rotateRight(u)
            } else {
                rotateLeft(u.leftChild!!)
                rotateRight(u)
            }
        } else if (balance == 2) {
            if (u.rightChild != null && getBalance(u.rightChild!!) >= 0) {
                rotateLeft(u)
            } else {
                rotateRight(u.rightChild!!)
                rotateLeft(u)
            }
        }
    }

    /**
     * Update the height information in the upstream tree and restore balance if necessary.
     * @param node starting point
     */
    override fun update(node: BinarySearchTree.NodeImpl<K, V>?) {
        var u = node
        while (u != null && u.parent != u) {
            updateLocalHeight(u)
            restoreBalance(u)
            u = u.parent
        }
    }

    /**
     * Return the height of the node, -1 if the node is null
     * @param node may be null
     * @return height or -1 if null
     */
    private fun getSafeHeight(node: BinarySearchTree.NodeImpl<K, V>?): Int {
        return node?.height ?: -1
    }

    /**
     * Return the balance of the given node.
     * @param node Node should not be null.
     * @return balance of node
     */
    private fun getBalance(node: BinarySearchTree.NodeImpl<K, V>): Int {
        return getSafeHeight(node.rightChild) - getSafeHeight(node.leftChild)
    }
}
