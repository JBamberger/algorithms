package de.jbamberger.algorithms.graph

/**
 * @author Jannik Bamberger (dev.jbamberger@gmail.com)
 */


data class Traversal<N>(val data: N, val seqNr: Int, val parentId: Int)

//fun <N,E>Graph<N,  E>.dfs(root: Node<N>): Graph<Traversal<N>, E> {
//    val S: Stack<Node<N>> = Stack();
//    val Q: Queue<N>
//
//
//
//}