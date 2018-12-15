package de.jbamberger.algorithms.map


/**
 * A Red-Black Tree is a mostly balanced binary search tree. To maintain a mostly balanced tree
 * some nodes are marked as exceptions to constraints that apply to a fully balanced tree.
 * In this case, the normal nodes are marked as 'black' and the exceptional nodes are 'red'.
 * The exceptional nodes are distributed, such that each leaf node has the exactly same depth,
 * if the exceptional nodes are not counted.
 *
 * The tree fulfills three constraints at all times:
 * Root condition: The root is always black
 * Color condition: Children of red nodes are black or external
 * Depth condition: All external nodes have the same black depth (number of black predecessors)
 *
 * External nodes are virtual nodes of the leaf nodes.
 *
 * The formulation uses external nodes to define the depth, because a tree where every node
 * has only one child is not balanced, but would fulfill the conditions.
 */
class RedBlackTree<K : Comparable<K>, V>() : SimpleSortedMutableMap<K, V> {

    override var size: Int = 0
        private set

    private var root: Node<K, V>? = null

    override fun containsKey(key: K): Boolean = getInternal(key) != null

    override fun get(key: K): V? = getInternal(key)?.value

    override fun entryRange(from: K, to: K): Iterable<Entry<K, V>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun put(key: K, value: V): V? {
        var node = root

        if (node == null) {
            root = Node(key, value, Color.BLACK)
            return null;
        }

        var prev: Node<K, V> = node

        while (node != null && node.key != key) {
            prev = node
            when {
                key < node.key -> node = node.left
                key > node.key -> node = node.right
            }
        }

        if (node?.key == key) {
            val oldValue = node.value
            node.value = value
            return oldValue
        } else {
            val newNode = Node(key, value, Color.RED)

            when {
                key < prev.key -> prev.makeLeftChild(newNode)
                key > prev.key -> prev.makeRightChild(newNode)
            }

            correctColors(prev, newNode)

            return null
        }
    }

    private fun correctColors(v: Node<K, V>, w: Node<K, V>) {
        fun canRecolor(u: Node<K, V>, v: Node<K, V>, z: Node<K, V>?): Boolean {
            if (z != null && z.color == Color.RED) { // recoloring is sufficient
                v.color = Color.BLACK
                z.color = Color.BLACK
                // if u is the root node, the value needs not be changed, because the black height
                // of the entire tree increased by one
                if (u != root) {
                    // if u is not the root node, the black height must be preserved -> color u red
                    u.color = Color.RED
                    // this might create another double-red situation at u.
                    // The parent of u is non-null, because u is not the root node
                    correctColors(u.parent!!, u)
                }
                return true // no rotations are required
            }
            return false // could not simply recolor -> rotations must be performed
        }


        if (v.color == Color.BLACK) { // there is not double-red -> no correction necessary
            return
        }
        val u = v.parent!!

        val colorRoot = when (v) {
            u.left -> {
                if (canRecolor(u, v, u.right)) {
                    return
                }

                when (w) {
                    v.left -> rotateRight(u, v)
                    v.right -> rotateRight(u, rotateLeft(v, w))
                    else -> throw IllegalStateException()
                }
            }
            u.right -> {
                if (canRecolor(u, v, u.left)) {
                    return
                }
                when (w) {
                    v.left -> rotateLeft(u, rotateRight(v, w))
                    v.right -> rotateLeft(u, v)
                    else -> throw IllegalStateException()
                }
            }
            else -> throw IllegalStateException()
        }

        colorRoot.color = Color.BLACK
        colorRoot.left!!.color = Color.RED
        colorRoot.right!!.color = Color.RED
    }

    override fun remove(key: K): V? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun getInternal(key: K): Node<K, V>? {
        var prev: Node<K, V>? = null
        var n: Node<K, V>? = root

        while (n != null) {
            prev = n
            when (n.key) {
                key == n.key -> return n
                key < n.key -> n = n.left
                key > n.key -> n = n.right
            }
        }
        return null
    }

    private fun getPredInternal(key: K): Node<K, V>? {
        var prev: Node<K, V>? = null
        var n: Node<K, V>? = root

        while (n != null) {
            when (n.key) {
                key == n.key -> return prev
                key < n.key -> n = n.left
                key > n.key -> n = n.right
            }
            prev = n
        }
        return prev
    }

    private fun rotateLeft(u: Node<K, V>, v: Node<K, V>): Node<K, V> {
        if (u == root) {
            root = v
            v.parent = null
        } else {
            val parent = u.parent!!
            when (u) {
                parent.left -> parent.makeLeftChild(v)
                parent.right -> parent.makeRightChild(v)
                else -> throw IllegalStateException()

            }
        }
        u.makeRightChild(v.left)
        v.makeLeftChild(u)
        return v
    }

    private fun rotateRight(u: Node<K, V>, v: Node<K, V>): Node<K, V> {
        if (u == root) {
            root = v
            v.parent = null
        } else {
            val parent = u.parent!!
            when (u) {
                parent.left -> parent.makeLeftChild(v)
                parent.right -> parent.makeRightChild(v)
                else -> throw IllegalStateException()

            }
        }
        u.makeLeftChild(v.right)
        v.makeRightChild(u)
        return v
    }


    enum class Color {
        RED, BLACK
    }

    class Node<K, V>(
            override val key: K,
            override var value: V,
            var color: Color
    ) : Entry<K, V> {
        var parent: Node<K, V>? = null
        var left: Node<K, V>? = null
        var right: Node<K, V>? = null

        fun makeLeftChild(node: Node<K, V>?) {
            this.left = node
            node?.parent = this.left
        }

        fun makeRightChild(node: Node<K, V>?) {
            this.right = node
            node?.parent = this.right
        }
    }
}