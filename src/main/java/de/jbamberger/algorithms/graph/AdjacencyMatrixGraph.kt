package de.jbamberger.algorithms.graph

import java.util.*
import kotlin.collections.HashMap

class CodeGraphTransform(
        private val initialNodeId: NodeId,
        private val graph: AdjacencyMatrixGraph<GraphNode, String>
) {

    private val initial: GraphNode = graph[initialNodeId]?.let {
        return@let when {
            it.type != NodeType.INITIAL -> throw IllegalArgumentException("Node 0 is not the initial node!")
            else -> it
        }
    } ?: throw IllegalArgumentException("No node 0.")

    private val initialEdge: Pair<NodeId, NodeId> = graph.getSuccessors(initialNodeId).let {
        return@let when {
            it.size != 1 -> throw IllegalArgumentException("Initial node must have exactly one outgoing edge.")
            else -> Pair(initialNodeId, it.first())
        }
    }

    private val visited: MutableMap<NodeId, Int> = HashMap()
    private val crossingEdge: MutableMap<Pair<NodeId, NodeId>, TraversalMark> = HashMap()

    fun convertToCode(): String {
        assertGraphStylePreTraversal()
        transformGraph()

        val (codeNodeId, codeNode) = requireNext(initialNodeId)
        if (codeNode.type != NodeType.COMPUTATION) {
            throw IllegalStateException("Transformation failed to produce useful graph. Expected computational node.")
        }
        val (finalNodeId, finalNode) = requireNext(codeNodeId)
        if (finalNode.type != NodeType.FINAL) {
            throw IllegalStateException("Transformation failed to produce useful graph. Expected final node.")
        }

        return codeNode.statements
    }

    /**
     * Transform the code graph, such that it contains only three nodes. One initial, one final and one code node.
     * The code node contains the entire formatted code generated from the graph.
     *
     * The graph is transformed by iteratively merging adjacent computational nodes and replacing control structures
     * with computational nodes. This reduces the size of the graph strictly monotonically until only three nodes
     * remain, if everything worked out as expected. If not, the graph contains structures which could not be handled
     * correctly, i.e. they are modeling errors.
     */
    private fun transformGraph() {
        // merge merge nodes
        // merge final nodes
        mergeAdjacentComputationalNodes()
        dfs()

        var oldGraphSize: Int
        do {
            oldGraphSize = graph.nodes().size
            mergeAdjacentComputationalNodes()
            replaceControlFlowBranches()
            replaceControlFlowLoops()
            dropUselessMergeNodes()



            //TODO: remove
            println("##########################################")
            println(graph.toString())
            println("##########################################")
        } while (oldGraphSize > graph.nodes().size)
    }

    private fun mergeAdjacentComputationalNodes() {
        val nodes = graph.filterNodes { it.type == NodeType.COMPUTATION }
        for (node in nodes.entries) {
            val (next, nextVal) = requireNext(node.key)
            if (nextVal.type == NodeType.COMPUTATION) {
                // set the values on next, because next might appear later in the list
                nextVal.statements = node.value.statements + "\n" + nextVal.statements
                graph.remove(node.key)
            }
        }
    }

    private fun dropUselessMergeNodes() {
        val mergeNodes = graph.filterNodes { it.type == NodeType.MERGE }

        for (node in mergeNodes) {
            val predecessors = graph.getPredecessors(node.key)
            if (predecessors.size == 1) {
                val prevId = predecessors.first()
                val next = requireNext(node.key)

                graph[prevId, next.first] = ""
                graph.remove(node.key)
            }
        }
    }

    private fun replaceControlFlowBranches() {
        val decisionNodes = graph.filterNodes { it.type == NodeType.DECISION }

        for (d in decisionNodes.entries) {
            val (a, b) = d.requireTwoOutgoing();

            if (a == b) {
                throw IllegalStateException("Parallel connections are not allowed.")
            }
            val aVal = graph[a]!!
            val bVal = graph[b]!!

            if (aVal.type == NodeType.COMPUTATION) {
                // a is the IF branch
                val (next, nextVal) = requireNext(a)

                if (nextVal.type != NodeType.MERGE) {
                    continue // this path contains more nodes that need to shrink into one.
                }

                // this branch is complete, we need to check, if the other branch is complete too
                if (next == b) {
                    replaceIf(d.key, a, b) // b ist the same merge node as the next after a, i.e. there is no else
                    continue
                }
                if (bVal.type != NodeType.COMPUTATION) {
                    continue // the branch contains no computation but exists -> more processing necessary
                }

                // b is a computation node
                val (bNext, bNextVal) = requireNext(b)

                if (bNext != next) {
                    continue // the else path is too complex and needs further processing
                }
                replaceIfElse(d.key, a, b, next)
                continue
            } else if (aVal.type == NodeType.MERGE) {
                // a is the non-existing else branch
                if (bVal.type != NodeType.COMPUTATION) {
                    continue // b is complex -> more processing necessary
                }
                val (next, nextVal) = requireNext(b)

                if (next != a) {
                    continue // the branch is complex -> more processing necessary
                }

                replaceIf(d.key, b, a)
                continue
            } else {
                continue // not a clear if
            }
        }
    }

    private fun replaceControlFlowLoops() {
        //TODO()
    }

    /**
     * Perform assertions on the graph to ensure it is in good shape, i.e. there are no forbidden elements.
     *
     * assert that the graph contains at least one computation node
     * assert that the graph contains no parallel edges
     *
     * assert graph has exactly one initial node
     * assert graph has exactly one final node
     *
     * assert that each code node has exactly one incoming and outgoing edge
     * assert that each conditional node has exactly one incoming edge and exactly two outgoing edges
     * assert that each merge node has exactly one outgoing edge
     *
     * assert that the graph is connected
     */
    private fun assertGraphStylePreTraversal() {
        //TODO()
    }

    /**
     * The depth first search assigns a number to each node. This traversal generates a spanning tree from the code
     * graph. All edges crossing over different branches are collected. Each such crossing edge has a direction, either
     * forward or backward. The direction is a hint to what kind of control structure must be generated.
     * For forward crossing edges (i.e. edge from low to high dfsNumber) indicates that the branch is part of a
     * conditional structure like if or switch. If the edge is backwards crossing it introduces a cycle into the
     * otherwise acyclic graph. Such a cycle gives a loop control structure.
     */
    private fun dfs() {
        var fdsNumber = 0
        val stack = ArrayDeque<Pair<NodeId, NodeId>>()
        visited[initialNodeId] = fdsNumber++
        stack.push(initialEdge)

        while (!stack.isEmpty()) {
            val edge = stack.pop()
            val node = edge.second
            if (node !in visited) {
                visited[node] = fdsNumber++
                graph.getSuccessors(node).forEach {
                    stack.push(Pair(node, it))
                }
            } else {
                val mark = if (visited[edge.first]!! < visited[edge.second]!!) {
                    TraversalMark.FORWARD_CROSSING
                } else {
                    TraversalMark.BACKWARD_CROSSING
                }
                crossingEdge[edge] = mark
            }
        }
    }

    private fun requireNext(node: NodeId): Pair<NodeId, GraphNode> {
        return graph.getSuccessors(node).let {
            when {
                it.size != 1 -> throw IllegalArgumentException("Node $node must have exactly one outgoing edge, has " + it.size)
                else -> {
                    val id = it.first()
                    return Pair(id, graph[id]!!)
                }
            }
        }
    }

    private fun nextNodeAssertSingle(node: NodeId): NodeId {
        return graph.getSuccessors(node).let {
            return@let when {
                it.size != 1 -> throw IllegalArgumentException("Initial node must have exactly one outgoing edge.")
                else -> it.first()
            }
        }
    }

    private fun Map.Entry<NodeId, GraphNode>.requireTwoOutgoing(): Pair<NodeId, NodeId> {
        val neighbors = graph.getSuccessors(this.key)

        if (neighbors.size != 2) throw IllegalStateException("Neighbor size must be two.")
        val i = neighbors.iterator()
        return Pair(i.next(), i.next())
    }

    private fun replaceIf(conditional: NodeId, ifBody: NodeId, mergeNode: NodeId) {
        val ifLabel = graph[conditional, ifBody]!!
        val elseLabel: String? = graph[conditional, mergeNode]!!
        val conditionalContent = graph[conditional]!!
        val ifBodyContent = graph[ifBody]!!.statements
        if (!elseLabel.equals("else", true)) {
            throw IllegalStateException("missing condition label else")
        }

        conditionalContent.statements = "IF($ifLabel) {\n$ifBodyContent\n}"
        conditionalContent.type = NodeType.COMPUTATION

        graph.remove(ifBody)
        graph.setEdge(conditional, mergeNode, "")
    }

    private fun replaceIfElse(conditional: NodeId, body1: NodeId, body2: NodeId, mergeNode: NodeId) {
        val conditionalContent = graph[conditional]!!

        val body1Content = graph[body1]!!.statements
        val body1Label = graph[conditional, body1]!!

        val body2Content = graph[body2]!!.statements
        val body2Label = graph[conditional, body2]!!

        fun ifElse(ifCondition: String, ifBody: String, elseBody: String) {
            conditionalContent.statements = "IF($ifCondition) {\n$ifBody\n} ELSE {\n$elseBody\n}"
            conditionalContent.type = NodeType.COMPUTATION
        }

        fun ifElseIf(ifCondition: String, ifBody: String, elseCondition: String, elseBody: String) {
            conditionalContent.statements = "IF($ifCondition) {\n$ifBody\n} ELSEIF ($elseCondition) {\n$elseBody\n}"
            conditionalContent.type = NodeType.COMPUTATION
        }

        if (body1Label.equals("else", true)) {
            ifElse(body2Label, body2Content, body1Content)
        } else if (body2Label.equals("else", true)) {
            ifElse(body1Label, body1Content, body2Content)
        } else {
            ifElseIf(body1Label, body1Content, body2Label, body2Content)
        }

        graph.remove(body1)
        graph.remove(body2)
        graph.setEdge(conditional, mergeNode, "")
    }
}



typealias NodeId = Int

class GraphNode(var type: NodeType, var statements: String) {
    override fun toString(): String {
        return StringBuilder().append("[").append(type).append("]\n").append(statements).append("\n").toString()
    }
}

enum class TraversalMark { FORWARD_CROSSING, BACKWARD_CROSSING }

enum class NodeType { INITIAL, COMPUTATION, DECISION, MERGE, FINAL }

class BidiMap<A, B> {
    private val d1: MutableMap<A, B> = HashMap()
    private val d2: MutableMap<B, A> = HashMap()

    operator fun set(a: A, b: B) {
        d1[a] = b
        d2[b] = a
    }

    fun getB(a: A) = d1[a]
    fun getA(b: B) = d2[b]

    fun removeA(a: A): B? {
        val bVal = d1.remove(a)
        if (bVal != null) d2.remove(bVal)
        return bVal
    }

    fun removeB(b: B): A? {
        val aVal = d2.remove(b)
        if (aVal != null) d1.remove(aVal)
        return aVal
    }
}

operator fun <K, V> HashMap<K, MutableList<V>>.set(key: K, value: V): Boolean {
    return computeIfAbsent(key) { LinkedList() }.add(value)
}

class AdjacencyMatrixGraph<N, E>(nodes: Map<NodeId, N>) {
    private val nodes: MutableMap<NodeId, N> = HashMap(nodes)
    private val adjacency: AdjacencyMatrix<E> = AdjacencyMatrix()

    fun nodes(): Map<NodeId, N> = nodes
    fun edges(): ImmutableAdjacencyMatrix<E> = adjacency

    fun nodes(ids: Set<NodeId>): Map<NodeId, N> {
        return nodes.filter { it.key in ids }
    }

    fun getSuccessors(node: NodeId): Set<NodeId> {
        return adjacency[node].keys
    }

    fun getPredecessors(node: NodeId): Set<NodeId> {
        return adjacency.getPrev(node).keys
    }


    inline fun filterNodes(predicate: (N) -> Boolean): Map<NodeId, N> {
        return nodes().filterValues { predicate.invoke(it) }
    }

    operator fun get(node: NodeId): N? {
        return nodes[node]
    }

    operator fun get(n1: NodeId, n2: NodeId): E? {
        return adjacency[n1, n2]
    }

    operator fun set(node: NodeId, value: N) {
        if (nodes.put(node, value) != null) {
            throw IllegalArgumentException("Duplicate nodes are note allowed.")
        }
    }

    fun setEdge(n1: NodeId, n2: NodeId, value: E) {
        adjacency.set(n1, n2, value)
    }

    operator fun set(n1: NodeId, n2: NodeId, value: E) {
        if (!(n1 in nodes && n2 in nodes)) {
            throw IllegalArgumentException("Node not in graph")
        }
        if (adjacency.set(n1, n2, value) != null) {
            throw IllegalArgumentException("Parallel edges are not allowed.")
        }
    }

    fun remove(n: NodeId) {
        nodes.remove(n)
        adjacency.remove(n)
    }

    override fun toString(): String {
        val b = StringBuilder()
        for (node in nodes) {
            b.append("Node ").append(node.key).append(" ").append(node.value).append('\n')
        }
        b.append(adjacency.toString())
        return b.toString()
    }
}

interface ImmutableAdjacencyMatrix<E> {
    operator fun get(n1: NodeId, n2: NodeId): E?
    operator fun get(n1: NodeId): Map<Int, E>?
}

class AdjacencyMatrix<E> : ImmutableAdjacencyMatrix<E> {
    private val container: MutableMap<NodeId, MutableMap<NodeId, E>> = HashMap()

    operator fun set(n1: NodeId, n2: Int, edge: E): E? {
        val m: MutableMap<NodeId, E> = container.computeIfAbsent(n1) { HashMap() }
        return m.put(n2, edge)
    }

    override operator fun get(n1: NodeId, n2: NodeId): E? {
        return container[n1]?.let { it[n2] }
    }

    override operator fun get(n1: NodeId): Map<NodeId, E> {
        return container[n1] ?: emptyMap()
    }

    fun getPrev(n: NodeId): Map<NodeId, E> {
        val out = HashMap<NodeId, E>()
        container.entries.forEach {
            val edge = it.value[n]
            if (edge != null) {
                out[it.key] = edge
            }
        }
        return out
    }

    fun remove(n: NodeId) {
        container.remove(n)
        container.values.forEach { it.remove(n) }
    }

    override fun toString(): String {
        val b = StringBuilder()
        for (from in container.entries) {
            for (to in from.value.entries) {
                b.append("Edge ").append(from.key).append(" -> ").append(to.key).append(" (").append(to.value).append(")\n")
            }
        }
        return b.toString()
    }
}