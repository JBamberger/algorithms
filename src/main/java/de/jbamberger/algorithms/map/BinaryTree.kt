package de.jbamberger.algorithms.map

import java.util.stream.Stream

interface BinaryTree<K : Comparable<K>, V> {
    fun getRoot(): Node<K, V>?
    fun size(): Int
    fun find(key: K): V?
    fun insert(key: K, value: V): V?
    fun delete(key: K): V?
    fun findInterval(min: K, max: K): Stream<V>

    interface Node<K, V> {
        fun getKey(): K?
        fun getValue(): V?
        fun getHeight(): Int
        fun getParent(): Node<K, V>?
        fun getLeftChild(): Node<K, V>?
        fun getRightChild(): Node<K, V>?
    }
}
