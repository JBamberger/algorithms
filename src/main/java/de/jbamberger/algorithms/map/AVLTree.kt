package de.jbamberger.algorithms.map

class AVLTree<K : Comparable<K>, V> : BinarySearchTree<K, V>() {

    /**
     * Rotate left at [u] and return the new root node.
     */
    private fun rotateLeft(u: BinarySearchTree.NodeImpl<K, V>): BinarySearchTree.NodeImpl<K, V> {
        val v = u.rightChild!!
        v.parent = u.parent
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
        u.rightChild?.parent = u
        v.leftChild = u
        u.parent = v
        updateLocalHeight(u)
        updateLocalHeight(v)
        return v
    }

    /**
     * Rotate right at [u] and return the new root node.
     */
    private fun rotateRight(u: BinarySearchTree.NodeImpl<K, V>): BinarySearchTree.NodeImpl<K, V> {
        val v = u.leftChild!!
        v.parent = u.parent
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
        u.leftChild?.parent = u
        v.rightChild = u
        u.parent = v
        updateLocalHeight(u)
        updateLocalHeight(v)
        return v
    }

    /**
     * If the balance is 2 or -2 the appropriate rotation is performed.
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
     * Update the height information in the upper tree, restoring balance if necessary.
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
     * Compute the balance of a node.
     */
    private fun getBalance(node: BinarySearchTree.NodeImpl<K, V>): Int {
        // height or -1 if null
        fun getSafeHeight(node: BinarySearchTree.NodeImpl<K, V>?): Int {
            return node?.height ?: -1
        }
        return getSafeHeight(node.rightChild) - getSafeHeight(node.leftChild)
    }
}
