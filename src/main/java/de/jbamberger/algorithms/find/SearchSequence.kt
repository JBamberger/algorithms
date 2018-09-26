package de.jbamberger.algorithms.find

/**
 * @author Jannik Bamberger (dev.jbamberger@gmail.com)
 */

interface SearchableSet<K : Comparable<K>, V> {
    fun insert(key: K, value: V): V?
    fun remove(key: K): V?
    fun find(key: K): V?
    fun size(): Int
}

class MoveToFrontList<K : Comparable<K>, V> : SearchableSet<K, V> {

    var head: Node<K, V>? = null
    var size: Int = 0

    override fun insert(key: K, value: V): V? {
        val (prev, found) = findNode(key)

        // create new node and put it at the front
        if (found == null) {
            val n = Node(null, head, key, value)
            head?.prev = n
            head = n
            size++
            return null
        }

        moveToFront(prev, found)

        // return the found value
        val v = found.value
        found.value = value
        return v
    }

    override fun remove(key: K): V? {
        val (prev, found) = findNode(key)
        if (found == null) return null

        when (prev) {
            null -> head = found.next
            else -> prev.next = found.next
        }
        size--
        return found.value
    }

    override fun find(key: K): V? {
        val (prev, found) = findNode(key)
        return found?.let {
            moveToFront(prev, found)
            found.value
        }
    }

    private fun findNode(key: K): Pair<Node<K, V>?, Node<K, V>?> {
        var prev: Node<K, V>? = null
        var next: Node<K, V>? = head
        while (next != null) {
            if (next.key == key) {
                return Pair(prev, next)
            } else {
                prev = next
                next = next.next
            }
        }
        return Pair(null, null)
    }

    private fun moveToFront(prev: Node<K, V>?, found: Node<K, V>) {
        if (prev != null) {
            prev.next = found.next
            found.next?.prev = prev
            found.next = head
            head!!.prev = found
            head = found
        } // otherwise the found node is the head already
    }

    override fun size() = size

    class Node<K, V>(
            var prev: Node<K, V>? = null,
            var next: Node<K, V>? = null,
            var key: K,
            var value: V
    )
}