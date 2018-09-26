package de.jbamberger.algorithms.map

class AVLTree<K : Comparable<K>, V> : BinarySearchTree<K, V>() {

    /**
     * Do a left rotation at node u and return the new subtree root.
     * @param u starting point
     * @return new subtree root
     */
    private fun rotateLeft(u: BinarySearchTree.NodeImpl<K, V>): BinarySearchTree.NodeImpl<K, V> {
        val v = u.getRightChild()
        v!!.setParent(u.getParent())
        if (u.getParent() != null) { // check if u is left or right child or node
            if (u.getParent()!!.getLeftChild() != null && u.getParent()!!.getLeftChild()!!.getKey() === u.getKey()) {
                u.getParent()!!.setLeftChild(v)
            } else {
                u.getParent()!!.setRightChild(v)
            }
        } else {
            root = v
        }
        u.setRightChild(v.getLeftChild())
        if (u.getRightChild() != null) {
            u.getRightChild()!!.setParent(u)
        }
        v.setLeftChild(u)
        u.setParent(v)
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
        val v = u.getLeftChild()
        v!!.setParent(u.getParent())
        if (u.getParent() != null) { // check if u is left or right child or node
            if (u.getParent()!!.getLeftChild() != null && u.getParent()!!.getLeftChild()!!.getKey() === u.getKey()) {
                u.getParent()!!.setLeftChild(v)
            } else {
                u.getParent()!!.setRightChild(v)
            }
        } else {
            root = v
        }
        u.setLeftChild(v.getRightChild())
        if (u.getLeftChild() != null) {
            u.getLeftChild()!!.setParent(u)
        }
        v.setRightChild(u)
        u.setParent(v)
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
            if (u.getLeftChild() != null && getBalance(u.getLeftChild()!!) <= 0) {
                rotateRight(u)
            } else {
                rotateLeft(u.getLeftChild()!!)
                rotateRight(u)
            }
        } else if (balance == 2) {
            if (u.getRightChild() != null && getBalance(u.getRightChild()!!) >= 0) {
                rotateLeft(u)
            } else {
                rotateRight(u.getRightChild()!!)
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
        while (u != null && u.getParent() != u) {
            updateLocalHeight(u)
            restoreBalance(u)
            u = u.getParent()
        }
    }

    /**
     * Return the height of the node, -1 if the node is null
     * @param node may be null
     * @return height or -1 if null
     */
    private fun getSafeHeight(node: BinarySearchTree.NodeImpl<K, V>?): Int {
        return node?.getHeight() ?: -1
    }

    /**
     * Return the balance of the given node.
     * @param node Node should not be null.
     * @return balande of node
     */
    private fun getBalance(node: BinarySearchTree.NodeImpl<K, V>): Int {
        return getSafeHeight(node.getRightChild()) - getSafeHeight(node.getLeftChild())
    }
}
