package de.jbamberger.algorithms.find

/**
 * @author Jannik Bamberger (dev.jbamberger@gmail.com)
 */

interface Dictionary<K : Comparable<K>, V> {
    fun size(): Int
    fun find(key: K): V?
    fun insert(key: K, value: V): V?
    fun delete(key: K): V?
}

class BinTree<K : Comparable<K>, V> : Dictionary<K, V> {
    private var size = 0
    private var root: Node<K, V>? = null

    override fun size(): Int {
        return size
    }

    private fun search(root: Node<K, V>, key: K): Node<K, V>? {
        var v: Node<K, V>? = root
        while (v != null && key != v.key) {
            v = (if (key < v.key) v.left else v.right)
        }
        return v
    }

    override fun find(key: K): V? {
        val v = root?.let { search(it, key) }
        return v?.value
    }

    override fun insert(key: K, value: V): V? {
        var v = root
        if (v == null) {
            root = Node(key, value, null, null)
            size = 1
            return value
        } else {
            var u: Node<K, V> = v
            while (v != null && key != v.key) {
                u = v
                v = if (key < v.key) v.left else v.right
            }
            if (key == v?.key) {
                return null; // already found
            } else {
                v = Node(key, value, null, null)
                if (key < u.key) {
                    u.left = v
                } else {
                    u.right = v
                }
                return value
            }
        }
    }

    override fun delete(key: K): V? {
        var v: Node<K, V>? = root
        var parent: Node<K, V> = v!!
        while (v != null && key != v.key) {
            parent = v
            v = if (key < v.key) v.left else v.right
        }
        if (v == null || v.key != key) return null
        val value = v.value

        if (v.left != null) { // has left sub-tree
            val toBeDeleted = v

            var parentOfLargest = v
            var largestChild = v.left!!

            // find the largest node in the left subtree
            while (largestChild.right != null) {
                parentOfLargest = largestChild
                largestChild = largestChild.right!!
            }
            // largestOfLeftTree is the largest node in the left tree
            // move largest node from left tree into position that is deleted
            toBeDeleted.key = largestChild.key
            toBeDeleted.value = largestChild.value

            val leftSubtreeOfLargest = largestChild.left

            if (largestChild === toBeDeleted.left) {
                toBeDeleted.left = leftSubtreeOfLargest
            } else {
                parentOfLargest!!.right = leftSubtreeOfLargest
            }
        } else {// there is no left subtree
            val rChild = v.right

            if (v === root) {
                root = rChild
            } else {
                if (key < parent.key) {
                    parent.left = rChild
                } else {
                    parent.right = rChild
                }
            }
        }
        size--
        return value
    }


    class Node<K : Comparable<K>, V>(var key: K, var value: V, var left: Node<K, V>?, var right: Node<K, V>?)
}

