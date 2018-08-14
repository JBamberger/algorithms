package de.jbamberger.algorithms.find

/**
 * @author Jannik Bamberger (dev.jbamberger@gmail.com)
 */

import java.util.stream.Stream

class BinarySearchTree<K : Comparable<K>, V> : BinaryTree<K, V> {
    override val root: BinaryTree.Node<K, V>?
        get() = _root
    private var _root: TreeNode? = null
    private var size: Int = 0

    init {
        this._root = null
        this.size = 0
    }

    private fun print(n: TreeNode?): String {
        return if (n == null) {
            ""
        } else {
            "(" + print(n.leftChild) + "" + n.value + "" + print(n.rightChild) + ")"
        }
    }

    override fun size(): Int {
        return this.size
    }

    override fun find(key: K): V? {
        val v = search(key)
        return v?.value
    }

    private fun search(key: K): BinaryTree.Node<K, V>? {
        var v: BinaryTree.Node<K, V>? = _root
        while (v != null && v.key != key) {
            if (key.compareTo(v.key) < 0) {
                v = v.leftChild
            } else {
                v = v.rightChild
            }
        }
        return v
    }

    override fun insert(key: K, value: V): V? {
        if (_root == null) { //tree is empty
            this._root = TreeNode(key, value)
            this._root!!.parent = this._root
        } else {
            var next = _root
            var current: TreeNode = _root!!
            while (next != null && key != next.key) {
                current = next
                if (key < next.key) {
                    next = next.leftChild
                } else {
                    next = next.rightChild
                }
            }
            if (next != null && key == next.key) { //key already exists
                return null
            } else {
                next = TreeNode(key, value)
                if (key < current.key) {
                    current.setLeft(next)
                } else {
                    current.setRight(next)
                }
                next.parent = current
            }
            update(next) //update height from insertion upwards
        }
        size++//new node created, so the size is one bigger.
        return value
    }

    override fun delete(key: K): V? {
        var v = search(key) as TreeNode?
        var value: V? = null
        if (v != null) {
            var u: TreeNode?
            val w: TreeNode?
            value = v.value
            if (v.leftChild != null) {
                u = v
                v = v.leftChild
                while (v!!.rightChild != null) { // find the rightmost element in the left subtree to sustain the order
                    v = v.rightChild
                }
                //replace u with found element
                u.key = v.key
                u.value = v.value

                w = v.leftChild //remaining subtree
                if (v == u.leftChild) {
                    u.setLeft(w)
                } else {
                    u = v.parent
                    u!!.setRight(w)
                }
            } else { // there is no left subtree, so the right subtree can be moved up.
                w = v.rightChild
                if (v == _root) {
                    _root = w
                    u = w
                } else {
                    u = v.parent
                    if (key < u!!.key) {
                        u.setLeft(w)
                    } else {
                        u.setRight(w)
                    }
                }
            }
            if (w != null) {
                w.parent = u
            }
            update(u) //update height upwards
            size-- //node deleted, decrement size
        }
        return value
    }

    /**
     * assumes nodes below are correct and all nodes are linked correctly.
     */
    private fun update(node: TreeNode?) {
        if (node != null) {
            if (node.leftChild != null) {
                if (node.rightChild != null) {
                    node.height = 1 + Math.max(node.leftChild!!.height!!, node.rightChild!!.height!!)
                } else {
                    node.height = 1 + node.leftChild!!.height!!
                }
            } else if (node.rightChild != null) {
                node.height = 1 + node.rightChild!!.height!!
            } else {
                node.height = 0
            }
            if (2 + node.height!! >= node.parent!!.height!!) { //value above may be wrong, update recursively
                if (node.key != node.parent!!.key) {
                    update(node.parent)

                }
            }
        }
    }


    override fun findInterval(min: K, max: K): Stream<V> {
        fun Stream.Builder<V>.interval(min: K, max: K, root: TreeNode?) {
            if (root == null) return

            if (min <= root.key) interval(min, max, root.leftChild)
            if (root.key in min..max) add(root.value)
            if (root.key <= max) interval(min, max, root.rightChild)
        }

        val builder = Stream.builder<V>()
        builder.interval(min, max, _root)
        return builder.build()
    }

    private inner class TreeNode(override var key: K, override var value: V) : BinaryTree.Node<K, V> {
        override var height: Int? = 0
        override var parent: TreeNode? = null
        override var leftChild: TreeNode? = null
            private set(value) {
                field = value
            }
        override var rightChild: TreeNode? = null
            private set(value) {
                field = value
            }

        fun setLeft(left: TreeNode?) {
            this.leftChild = left
        }

        fun setRight(right: TreeNode?) {
            this.rightChild = right
        }
    }
}

