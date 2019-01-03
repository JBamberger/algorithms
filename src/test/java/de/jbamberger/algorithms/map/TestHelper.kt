package de.jbamberger.algorithms.map

import java.util.*

/**
 * @author Jannik Bamberger (dev.jbamberger@gmail.com)
 */
object TestHelper {

    fun toString(tree: BinaryTree<*, *>, doOutputHeight: Boolean = false): String {
        fun inorderTraversal(l: MutableList<TreeNode<*, *>>, v: TreeNode<*, *>?) {
            if (v != null) {
                inorderTraversal(l, v.leftChild)
                l.add(v)
                inorderTraversal(l, v.rightChild)
            }
        }

        val l = LinkedList<TreeNode<*, *>>()
        inorderTraversal(l, tree.root)

        return makeString(l, doOutputHeight)
    }

    fun toStringPre(tree: BinaryTree<*, *>, doOutputHeight: Boolean): String {
        fun preorderTraversal(l: MutableList<TreeNode<*, *>>, v: TreeNode<*, *>?) {
            if (v != null) {
                l.add(v)
                preorderTraversal(l, v.leftChild)
                preorderTraversal(l, v.rightChild)
            }
        }

        val l = LinkedList<TreeNode<*, *>>()
        preorderTraversal(l, tree.root)
        return makeString(l, doOutputHeight)
    }

    fun toString(node: TreeNode<*, *>?) = "(${node!!.key},${node.value})"
    fun toStringWithHeight(node: TreeNode<*, *>) = "(${node.key},${node.value},${node.height})"

    private fun makeString(list: List<TreeNode<*, *>>, doOutputHeight: Boolean) =
            list.joinToString(separator = " ", transform = if (doOutputHeight) ::toStringWithHeight else ::toString)
}