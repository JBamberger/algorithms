package de.jbamberger.algorithms.graph

/**
 * @author Jannik Bamberger (dev.jbamberger@gmail.com)
 */


interface Graph<Id, NodePayload, EdgePayload> {
    val nodeCount: Int
    val edgeCount: Int
    val edges: Set<Edge<Id, EdgePayload>>
    val nodes: Set<Node<Id, NodePayload>>
}

interface MutableGraph<Id, NodePayload, EdgePayload> : Graph<Id, NodePayload, EdgePayload> {
    fun addNode(node: Node<Id, NodePayload>)
    fun addEdgeNode(node: Node<Id, NodePayload>, parent: Node<Id, NodePayload>)
    fun addEdge(edge: Edge<Id, EdgePayload>)
    fun removeEdge(edge: Edge<Id, EdgePayload>)
    fun removeEdgeAndOrphans(edge: Edge<Id, EdgePayload>)
    fun removeNode(node: Node<Id, NodePayload>)
}

interface DirectedGraph<Id, NodePayload, EdgePayload> : Graph<Id, NodePayload, EdgePayload>
interface UndirectedGraph<Id, NodePayload, EdgePayload> : Graph<Id, NodePayload, EdgePayload>

interface Node<Id, Payload> {
    val id: Id
    val payload: Payload
}

interface Edge<Id, Payload> {
    val from: Id
    val to: Id
    val payload: Payload
}

