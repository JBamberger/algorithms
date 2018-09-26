package de.jbamberger.algorithms.map

import java.util.stream.Stream


open class BinarySearchTree<K : Comparable<K>, V> : BinaryTree<K, V> {
    protected var size: Int = 0
    protected var root: NodeImpl<K, V>? = null

    /**
     * Search a node with the given `key` in the sub tree rooted at `root`.
     *
     * @param root root of the subtree
     * @param key  key to search for
     * @return the [NodeX] with the given key, or @{code null} if no such node exists
     */
    protected fun search(root: NodeImpl<K, V>?, key: K): NodeImpl<K, V> {
        var v = root
        var u: NodeImpl<K, V>? = null
        while (v != null && key.compareTo(v.getKey()!!) != 0) {
            u = v
            if (key.compareTo(v.getKey()!!) < 0) {
                v = v.getLeftChild()
            } else {
                v = v.getRightChild()
            }
        }
        return v ?: NodeImpl<K, V>(null, null, u, null, null)
    }

    override fun find(key: K): V? {
        val v = search(root, key)
        return if (v.getKey() != null) {
            v.getValue()
        } else {
            null // key not found
        }
    }

    override fun insert(key: K, value: V): V? {
        if (root == null) {
            // tree empty, create new root, no height update needed
            root = NodeImpl(key, value)
            size = 1
        } else {
            // otherwise, search the leaf u where the new node must be appended
            var v = search(root, key)
            if (v.getKey() != null && key.compareTo(v.getKey()!!) == 0) {
                // leaf u found, key already stored in the tree
                return null
            } else {
                // not fount, create new node with parent
                v = NodeImpl(key, value, v.getParent()!!, null, null)
                // the new node is left or right child, according to
                // the search tree property
                if (key.compareTo(v.getParent()!!.getKey()!!) < 0) {
                    v.getParent()!!.setLeftChild(v)
                } else {
                    v.getParent()!!.setRightChild(v)
                }
                size++
                // update height, u and all its ancestor might have wrong
                // height information
                update(v.getParent())
            }
        }
        return value
    }

    override fun delete(key: K): V? {
        var value: V? = null
        var v: NodeImpl<K, V>? = search(root, key)
        if (v!!.getKey() != null) {
            value = v.getValue()

            var u: NodeImpl<K, V>?
            val w: NodeImpl<K, V>? // temporary nodes
            // at the end, u will be the node where update has to start

            if (v.getLeftChild() != null) {
                /*--- case 1: there exists a left subtree at v ---*/
                u = v // store the "position" of v

                // search for the node with largest key in the subtree of u
                v = v.getLeftChild()
                while (v!!.getRightChild() != null) v = v.getRightChild()
                // v is now the node with largest key in the left subtree of the original v (=u)
                // "copy" the content of v to u (v will be deleted later)
                u.setKey(v.getKey())
                u.setValue(v.getValue())

                // now: delete v
                // here we know that the "largest" node v has no right subtree

                // store the root of the left subtree to w (might be null)
                // w needs to get a new parent later
                w = v.getLeftChild()

                // case 1a: the "largest" node is the child of u
                if (v === u.getLeftChild()) {
                    u.setLeftChild(w)
                } else {
                    u = v.getParent() // store in u, for later parent and height update
                    u!!.setRightChild(w)
                }// case 1b: the "largest" node is further down in the tree,
                // it is the right child of its parent, and has no right subtree
                // -> make v's left subtree to v.parent's right subtree
            } else {
                /*--- case 2: no left subtree at v ---*/
                w = v.getRightChild() // the single child of v

                // case 2a: v is root node
                if (v === root) {
                    root = w
                    u = w
                } else {
                    u = v.getParent()
                    if (key.compareTo(u!!.getKey()!!) < 0) {
                        u.setLeftChild(w)
                    } else {
                        u.setRightChild(w)
                    }
                }// case 2b: v is an internal node
                // connect v.parent to w at the correct side
            }
            // update parent pointer
            w?.setParent(u!!)
            // the deleted node was between u and w,
            // therefore the height information in the subtree of w is still
            // correct, and also for siblings of w.
            // However, node u and its ancestors need to update their height
            update(u)
            // node deleted (all references removed) -> decrease size
            size--
        }
        return value
    }

    override fun getRoot(): BinaryTree.Node<K, V>? {
        return root
    }

    override fun size(): Int {
        return size
    }

    /**
     * utility method to locally update the height information at a single node.
     */
    protected fun updateLocalHeight(node: NodeImpl<K, V>?) {
        if (node != null) {
            if (node.getLeftChild() == null && node.getRightChild() == null) {
                // trivial case: node is a leaf
                node.setHeight(0)
            } else {
                // otherwise: height is height of larger subtree + 1
                val leftHeight = if (node.getLeftChild() == null) 0 else node.getLeftChild()!!.getHeight()
                val rightHeight = if (node.getRightChild() == null) 0 else node.getRightChild()!!.getHeight()
                node.setHeight(1 + Math.max(leftHeight, rightHeight))
            }
        }
    }

    protected open fun update(node: NodeImpl<K, V>?) {
        var node: NodeImpl<K, V>? = node ?: return
// update height at the given node
        updateLocalHeight(node)
        // traverse up to root and update height
        while (node !== root) {
            node = node!!.getParent() // node is not root, therefore has a parent node
            updateLocalHeight(node)
        }
    }

    override fun findInterval(min: K, max: K): Stream<V> {
        val b = Stream.builder<V>()
        // check input
        if (max.compareTo(min) < 0) {
            System.err.println("Minimum is not less or equal than maximum")
        }
        // search for the nodes
        findAll(b, root, min, max)
        return b.build()
    }

    private fun findAll(list: Stream.Builder<V>, node: BinaryTree.Node<K, V>?, min: K, max: K) {
        if (node != null) {
            if (node.getKey()!!.compareTo(max) > 0) {
                // current key is greater than max -> continue search only in left subtree
                findAll(list, node.getLeftChild(), min, max)
            } else if (node.getKey()!!.compareTo(min) < 0) {
                // current key is smaller than min -> continue search only in right subtree
                findAll(list, node.getRightChild(), min, max)
            } else {
                // current key is in the interval -> perform regular "in-order" traversal
                findAll(list, node.getLeftChild(), min, max)
                list.accept(node.getValue())
                findAll(list, node.getRightChild(), min, max)
            }
        }
    }

    class NodeImpl<K : Comparable<K>, V> : BinaryTree.Node<K, V> {
        private var key: K? = null
        private var value: V? = null
        private var height: Int = 0
        private var parent: NodeImpl<K, V>? = null
        private var leftChild: NodeImpl<K, V>? = null
        private var rightChild: NodeImpl<K, V>? = null

        constructor(key: K?, value: V?) {
            this.key = key
            this.value = value
        }

        constructor(
                key: K?,
                value: V?,
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

        override fun getHeight(): Int {
            return height
        }

        fun setHeight(height: Int) {
            this.height = height
        }

        override fun getKey(): K? {
            return key
        }

        fun setKey(key: K?) {
            this.key = key
        }

        override fun getValue(): V? {
            return value
        }

        fun setValue(value: V?) {
            this.value = value
        }

        override fun getParent(): NodeImpl<K, V>? {
            return parent
        }

        fun setParent(parent: NodeImpl<K, V>?) {
            this.parent = parent
        }

        override fun getLeftChild(): NodeImpl<K, V>? {
            return leftChild
        }

        fun setLeftChild(leftChild: NodeImpl<K, V>?) {
            this.leftChild = leftChild
        }

        override fun getRightChild(): NodeImpl<K, V>? {
            return rightChild
        }

        fun setRightChild(rightChild: NodeImpl<K, V>?) {
            this.rightChild = rightChild
        }
    }
}
