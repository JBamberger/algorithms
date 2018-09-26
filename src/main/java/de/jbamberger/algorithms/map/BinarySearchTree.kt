package de.jbamberger.algorithms.map

import java.util.stream.Stream


open class BinarySearchTree<K : Comparable<K>, V> : BinaryTree<K, V> {

    override var size: Int = 0
        internal set

    override var root: NodeImpl<K, V>? = null
        internal set

    /**
     * Search for a key in the subtree below [root].
     */
    private fun search(root: NodeImpl<K, V>?, key: K): Pair<Boolean, NodeImpl<K, V>?> {
        var v = root
        var u: NodeImpl<K, V>? = null
        while (v != null && key.compareTo(v.key) != 0) {
            u = v
            v = if (key < v.key) v.leftChild else v.rightChild
        }
        return when (v) {
            null -> Pair(false, u)
            else -> Pair(true, v)
        }
    }

    /**
     * Find the key in the tree and return its value or null, if it is not found.
     */
    override fun get(key: K): V? {
        val (found, node) = search(root, key)
        return if (found) node!!.value else null
    }

    /**
     * Insert the new key + value into the key. If there is already a value for the key, the value is replaced and
     * returned.
     */
    override fun put(key: K, value: V): V? {
        // empty tree
        if (root == null) {
            root = NodeImpl(key, value)
            this.size = 1
            return value
        }

        var (found, v) = search(root, key)

        // found but with wrong key ?!
        if (found && key.compareTo(v!!.key) == 0) {
            return null
        }

        v = NodeImpl(key, value, v, null, null)
        // insert such that the order is maintained
        if (key < v.parent!!.key) {
            v.parent!!.leftChild = v
        } else {
            v.parent!!.rightChild = v
        }
        this.size++
        update(v.parent)


        return value
    }

    /**
     * Remove the key from the tree and return the value if there was one.
     */
    override fun remove(key: K): V? {
        var (found, v) = search(root, key)

        if (!found) return null

        val value = v!!.value

        var u: NodeImpl<K, V>?
        val w: NodeImpl<K, V>?

        if (v.leftChild != null) { // there is a left subtree at v
            u = v

            // find largest key in subtree of u
            v = v.leftChild
            while (v!!.rightChild != null) v = v.rightChild

            // v has the larges key in the left subtree
            u.key = v.key
            u.value = v.value

            w = v.leftChild

            if (v === u.leftChild) { // largest node is child of u
                u.leftChild = w
            } else { // the largest node is farther below
                u = v.parent
                u!!.rightChild = w
            }
        } else { // there is no left subtree
            w = v.rightChild

            if (v === root) { // v is the root
                root = w
                u = w
            } else { // v is an internal node
                u = v.parent
                if (key < u!!.key) {
                    u.leftChild = w
                } else {
                    u.rightChild = w
                }
            }
        }

        w?.parent = u
        update(u)
        size--

        return value
    }

    /**
     * Updates the height for one specific node.
     */
    protected fun updateLocalHeight(node: NodeImpl<K, V>?) {
        if (node == null) return
        if (node.leftChild == null && node.rightChild == null) {
            node.height = 0
        } else {
            val leftHeight = node.leftChild?.height ?: 0
            val rightHeight = node.rightChild?.height ?: 0
            node.height = 1 + Math.max(leftHeight, rightHeight)
        }
    }

    protected open fun update(node: NodeImpl<K, V>?) {
        var n: NodeImpl<K, V> = node ?: return

        updateLocalHeight(n)
        while (n !== root) {
            n = n.parent!! // n is not root, therefore it has a parent
            updateLocalHeight(n)
        }
    }

    override fun range(min: K, max: K): Stream<V> {
        if (max < min) throw IllegalArgumentException("Minimum is not less or equal than maximum")

        val b = Stream.builder<V>()
        findRange(b, root, min, max)
        return b.build()
    }

    private fun findRange(list: Stream.Builder<V>, node: BinaryTree.Node<K, V>?, min: K, max: K) {
        if (node == null) return

        when {
            node.key > max -> findRange(list, node.leftChild, min, max)
            node.key < min -> findRange(list, node.rightChild, min, max)
            else -> {
                findRange(list, node.leftChild, min, max)
                list.accept(node.value)
                findRange(list, node.rightChild, min, max)
            }
        }
    }

    class NodeImpl<K : Comparable<K>, V> : BinaryTree.Node<K, V> {
        override var key: K
            internal set
        override var value: V
            internal set
        override var height: Int = 0
            internal set
        override var parent: NodeImpl<K, V>? = null
            internal set
        override var leftChild: NodeImpl<K, V>? = null
            internal set
        override var rightChild: NodeImpl<K, V>? = null
            internal set

        constructor(key: K, value: V) {
            this.key = key
            this.value = value
        }

        constructor(
                key: K,
                value: V,
                parent: NodeImpl<K, V>?,
                leftChild: NodeImpl<K, V>?,
                rightChild: NodeImpl<K, V>?
        ) {
            this.key = key
            this.value = value
            this.parent = parent
            this.leftChild = leftChild
            this.rightChild = rightChild
        }
    }
}
