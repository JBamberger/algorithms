package de.jbamberger.algorithms.graph

import org.junit.Test

class CodeGraphTransformTest {

    @Test
    fun test() {
        val graph = AdjacencyMatrixGraph<GraphNode, String>(emptyMap())
        graph[0] = GraphNode(NodeType.INITIAL, "")
        graph[1] = GraphNode(NodeType.DECISION, "IF")
        graph[2] = GraphNode(NodeType.COMPUTATION, "int i = 0;")
        graph[3] = GraphNode(NodeType.MERGE, "")
        graph[4] = GraphNode(NodeType.FINAL, "")
        graph[5] = GraphNode(NodeType.DECISION, "IF2")


        graph[0,1] = ""
        graph[1,5] = "SOMETIMES"
        graph[5,2] = "MAYBE"
        graph[2,3] = ""
        graph[1,3] = "ELSE"
        graph[5,3] = "ELSE"
        graph[3,4] = ""

        println(graph.toString())

        println(CodeGraphTransform(0, graph).convertToCode())
    }
}