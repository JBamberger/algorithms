package de.jbamberger.algorithms.find

import java.util.stream.Stream

interface BinaryTree<K : Comparable<K>, V> {

    val root: Node<K, V>?
    fun size(): Int
    fun find(key: K): V?
    fun insert(key: K, value: V): V?
    fun delete(key: K): V?
    fun findInterval(min: K, max: K): Stream<V>

    interface Node<K, V> {
        val key: K
        val value: V
        val height: Int?
        val parent: Node<K, V>?
        val leftChild: Node<K, V>?
        val rightChild: Node<K, V>?
    }
}
