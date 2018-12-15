package de.jbamberger.algorithms.map


interface SimpleMap<K, V> {
    val size: Int
    fun isEmpty(): Boolean = size == 0
    fun containsKey(key: K): Boolean
    operator fun get(key: K): V?
}

interface SimpleSortedMap<K : Comparable<K>, V> : SimpleMap<K, V> {
    fun entryRange(from: K, to: K): Iterable<Entry<K, V>>
    fun keyRange(from: K, to: K): Iterable<K> = entryRange(from, to).map(Entry<K, V>::key)
    fun valueRange(from: K, to: K): Iterable<V> = entryRange(from, to).map(Entry<K, V>::value)
}

interface SimpleMutableMap<K, V> : SimpleMap<K, V> {
    override val size: Int
    operator fun set(key: K, value: V): V? = put(key, value)
    fun put(key: K, value: V): V?
    fun remove(key: K): V?
}

interface SimpleSortedMutableMap<K : Comparable<K>, V> : SimpleMutableMap<K, V>, SimpleSortedMap<K, V>


interface Entry<K, V> {
    val key: K
    val value: V
}
