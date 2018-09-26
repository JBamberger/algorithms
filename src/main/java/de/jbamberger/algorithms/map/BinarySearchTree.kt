package de.jbamberger.algorithms.map

import java.util.stream.Stream


open class BinarySearchTree<K : Comparable<K>, V> : BinaryTree<K, V> {

    override var size: Int = 0
        internal set(value) {
            field = value
        }

    override var root: NodeImpl<K, V>? = null
        internal set

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
        while (v != null && key.compareTo(v.key!!) != 0) {
            u = v
            if (key.compareTo(v.key!!) < 0) {
                v = v.leftChild
            } else {
                v = v.rightChild
            }
        }
        return v ?: NodeImpl<K, V>(null, null, u, null, null)
    }

    override fun get(key: K): V? {
        val v = search(root, key)
        return if (v.key != null) {
            v.value
        } else {
            null // key not found
        }
    }

    override fun put(key: K, value: V): V? {
        if (root == null) {
            // tree empty, create new root, no height update needed
            root = NodeImpl(key, value)
            this.size = 1
        } else {
            // otherwise, search the leaf u where the new node must be appended
            var v = search(root, key)
            if (v.key != null && key.compareTo(v.key!!) == 0) {
                // leaf u found, key already stored in the tree
                return null
            } else {
                // not fount, create new node with parent
                v = NodeImpl(key, value, v.parent!!, null, null)
                // the new node is left or right child, according to
                // the search tree property
                if (key.compareTo(v.parent!!.key!!) < 0) {
                    v.parent!!.leftChild = v
                } else {
                    v.parent!!.rightChild = v
                }
                this.size++
                // update height, u and all its ancestor might have wrong
                // height information
                update(v.parent)
            }
        }
        return value
    }

    override fun remove(key: K): V? {
        var value: V? = null
        var v: NodeImpl<K, V>? = search(root, key)
        if (v!!.key != null) {
            value = v.value

            var u: NodeImpl<K, V>?
            val w: NodeImpl<K, V>? // temporary nodes
            // at the end, u will be the node where update has to start

            if (v.leftChild != null) {
                /*--- case 1: there exists a left subtree at v ---*/
                u = v // store the "position" of v

                // search for the node with largest key in the subtree of u
                v = v.leftChild
                while (v!!.rightChild != null) v = v.rightChild
                // v is now the node with largest key in the left subtree of the original v (=u)
                // "copy" the content of v to u (v will be deleted later)
                u.key = v.key
                u.value = v.value

                // now: remove v
                // here we know that the "largest" node v has no right subtree

                // store the root of the left subtree to w (might be null)
                // w needs to get a new parent later
                w = v.leftChild

                // case 1a: the "largest" node is the child of u
                if (v === u.leftChild) {
                    u.leftChild = w
                } else {
                    u = v.parent // store in u, for later parent and height update
                    u!!.rightChild = w
                }// case 1b: the "largest" node is further down in the tree,
                // it is the right child of its parent, and has no right subtree
                // -> make v's left subtree to v.parent's right subtree
            } else {
                /*--- case 2: no left subtree at v ---*/
                w = v.rightChild // the single child of v

                // case 2a: v is root node
                if (v === root) {
                    root = w
                    u = w
                } else {
                    u = v.parent
                    if (key.compareTo(u!!.key!!) < 0) {
                        u.leftChild = w
                    } else {
                        u.rightChild = w
                    }
                }// case 2b: v is an internal node
                // connect v.parent to w at the correct side
            }
            // update parent pointer
            w?.parent = u
            // the deleted node was between u and w,
            // therefore the height information in the subtree of w is still
            // correct, and also for siblings of w.
            // However, node u and its ancestors need to update their height
            update(u)
            // node deleted (all references removed) -> decrease size
            this.size--
        }
        return value
    }

    /**
     * utility method to locally update the height information at a single node.
     */
    protected fun updateLocalHeight(node: NodeImpl<K, V>?) {
        if (node != null) {
            if (node.leftChild == null && node.rightChild == null) {
                // trivial case: node is a leaf
                node.height = 0
            } else {
                // otherwise: height is height of larger subtree + 1
                val leftHeight = if (node.leftChild == null) 0 else node.leftChild!!.height
                val rightHeight = if (node.rightChild == null) 0 else node.rightChild!!.height
                node.height = 1 + Math.max(leftHeight, rightHeight)
            }
        }
    }

    protected open fun update(node: NodeImpl<K, V>?) {
        var node: NodeImpl<K, V>? = node ?: return
// update height at the given node
        updateLocalHeight(node)
        // traverse up to root and update height
        while (node !== root) {
            node = node!!.parent // node is not root, therefore has a parent node
            updateLocalHeight(node)
        }
    }

    override fun range(min: K, max: K): Stream<V> {
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
            if (node.key!!.compareTo(max) > 0) {
                // current key is greater than max -> continue search only in left subtree
                findAll(list, node.leftChild, min, max)
            } else if (node.key!!.compareTo(min) < 0) {
                // current key is smaller than min -> continue search only in right subtree
                findAll(list, node.rightChild, min, max)
            } else {
                // current key is in the interval -> perform regular "in-order" traversal
                findAll(list, node.leftChild, min, max)
                list.accept(node.value)
                findAll(list, node.rightChild, min, max)
            }
        }
    }

    class NodeImpl<K : Comparable<K>, V> : BinaryTree.Node<K, V> {
        override var key: K? = null
            internal set
        override var value: V? = null
            internal set
        override var height: Int = 0
            internal set
        override var parent: NodeImpl<K, V>? = null
            internal set
        override var leftChild: NodeImpl<K, V>? = null
            internal set
        override var rightChild: NodeImpl<K, V>? = null
            internal set

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

//        fun setHeight(height: Int) {
//            this.height = height
//        }
//
//        fun setKey(key: K?) {
//            this.key = key
//        }
//
//        fun setValue(value: V?) {
//            this.value = value
//        }
//
//        fun setParent(parent: NodeImpl<K, V>?) {
//            this.parent = parent
//        }
//
//        fun setLeftChild(leftChild: NodeImpl<K, V>?) {
//            this.leftChild = leftChild
//        }
//
//        fun setRightChild(rightChild: NodeImpl<K, V>?) {
//            this.rightChild = rightChild
//        }
    }
}
