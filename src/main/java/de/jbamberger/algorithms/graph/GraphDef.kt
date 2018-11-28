package de.jbamberger.algorithms.graph

/**
 * @author Jannik Bamberger (dev.jbamberger@gmail.com)
 */


interface Graph<NodePayload, EdgePayload> {
    val nodeCount: Int
    val edgeCount: Int
    val edges: Set<Edge<EdgePayload>>
    val nodes: Set<Node<NodePayload>>
}

interface MutableGraph<NodePayload, EdgePayload> : Graph<NodePayload, EdgePayload> {
    fun addNode(node: Node<NodePayload>)
    fun addEdgeNode(node: Node<NodePayload>, parent: Node<NodePayload>)
    fun addEdge(edge: Edge<EdgePayload>)
    fun removeEdge(edge: Edge<EdgePayload>)
    fun removeEdgeAndOrphans(edge: Edge<EdgePayload>)
    fun removeNode(node: Node<NodePayload>)
}

interface DirectedGraph<NodePayload, EdgePayload> : Graph<NodePayload, EdgePayload>
interface UndirectedGraph<NodePayload, EdgePayload> : Graph<NodePayload, EdgePayload>

interface Node<Payload> {
    val id: Int
    val payload: Payload
}

interface Edge<Payload> {
    val from: Int
    val to: Int
    val payload: Payload
}

