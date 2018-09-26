package de.jbamberger.algorithms.map

import java.util.stream.Stream


interface SortedMutableMap<K : Comparable<K>, V> {
    val size: Int
    operator fun get(key: K): V?
    fun put(key: K, value: V): V?
    fun remove(key: K): V?
    fun range(min: K, max: K): Stream<V>
}


interface BinaryTree<K : Comparable<K>, V> : SortedMutableMap<K, V> {
    val root: Node<K, V>?

    interface Node<K, V> {
        val key: K
        val value: V?
        val height: Int
        val parent: Node<K, V>?
        val leftChild: Node<K, V>?
        val rightChild: Node<K, V>?
    }
}
