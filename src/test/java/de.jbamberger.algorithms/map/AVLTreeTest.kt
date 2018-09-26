package de.jbamberger.algorithms.map


import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*

/**
 * Inspired by test cases provide for the lecture "Algorithmen und Datenstrukturen" held in the winter term 2016/17 at
 * the University of Konstanz.
 */
abstract class AVLTreeTest {

    /**
     * @return an instance of the class to be tested.
     */
    abstract fun getImplementation(): BinaryTree<Int, String>


    /**
     * AVL tree test -- Insert: Fall 1
     */
    @Test
    fun testInsertCase1() {
        val t = getImplementation()
        t.put(4, "4")
        t.put(3, "3")
        t.put(7, "7")
        t.put(11, "11")
        //tree before insertion
        assertEquals("(3,3,0) (4,4,2) (7,7,1) (11,11,0)", toString(t, true))
        assertEquals("(4,4,2) (3,3,0) (7,7,1) (11,11,0)", toStringPre(t, true))
        // Fall 1: u =  7, balance(u) = 2, v = 11, w = 13, v = b
        assertEquals("13", t.put(13, "13"))
        //tree after insertion
        assertEquals("(3,3,0) (4,4,2) (7,7,0) (11,11,1) (13,13,0)", toString(t, true))
        assertEquals("(4,4,2) (3,3,0) (11,11,1) (7,7,0) (13,13,0)", toStringPre(t, true))
    }

    /**
     * AVL tree test -- Insert: Fall 1'
     */
    @Test
    fun testInsertCase1Symmetric() {
        val t = getImplementation()
        t.put(7, "7")
        t.put(4, "4")
        //tree before insertion
        assertEquals("(4,4,0) (7,7,1)", toString(t, true))
        assertEquals("(7,7,1) (4,4,0)", toStringPre(t, true))
        // Fall 1': u = 7, balance(u) = -2, v = 4, w = 3, v = b
        t.put(3, "3")
        //tree after insertion
        assertEquals("(3,3,0) (4,4,1) (7,7,0)", toString(t, true))
        assertEquals("(4,4,1) (3,3,0) (7,7,0)", toStringPre(t, true))

    }

    /**
     * AVL tree test -- Insert: Fall 2
     */
    @Test
    fun testInsertCase2() {
        val t = getImplementation()
        t.put(4, "4")
        t.put(3, "3")
        t.put(11, "11")
        t.put(7, "7")
        t.put(13, "13")
        //tree before insertion
        assertEquals("(3,3,0) (4,4,2) (7,7,0) (11,11,1) (13,13,0)", toString(t, true))
        assertEquals("(4,4,2) (3,3,0) (11,11,1) (7,7,0) (13,13,0)", toStringPre(t, true))
        // Fall 2: u = 4, balance(u) = 2, v = 11, w = 7, w = b
        t.put(8, "8")
        //tree after insertion
        assertEquals("(3,3,0) (4,4,1) (7,7,2) (8,8,0) (11,11,1) (13,13,0)", toString(t, true))
        assertEquals("(7,7,2) (4,4,1) (3,3,0) (11,11,1) (8,8,0) (13,13,0)", toStringPre(t, true))
    }

    /**
     * AVL tree test -- Insert: Fall 2'
     */
    @Test
    fun testInsertCase2Symmetric() {
        val t = getImplementation()
        t.put(7, "7")
        t.put(4, "4")
        t.put(11, "11")
        t.put(3, "3")
        t.put(5, "5")
        t.put(8, "8")
        t.put(13, "13")
        t.put(1, "1")
        //tree before insertion
        assertEquals("(1,1,0) (3,3,1) (4,4,2) (5,5,0) (7,7,3) (8,8,0) (11,11,1) (13,13,0)", toString(t, true))
        assertEquals("(7,7,3) (4,4,2) (3,3,1) (1,1,0) (5,5,0) (11,11,1) (8,8,0) (13,13,0)", toStringPre(t, true))
        // Fall 2': u = 3, balance(u) = -2, v = 1 , w = 2, w = b
        t.put(2, "2")
        //tree after insertion
        assertEquals("(1,1,0) (2,2,1) (3,3,0) (4,4,2) (5,5,0) (7,7,3) (8,8,0) (11,11,1) (13,13,0)", toString(t, true))
        assertEquals("(7,7,3) (4,4,2) (2,2,1) (1,1,0) (3,3,0) (5,5,0) (11,11,1) (8,8,0) (13,13,0)", toStringPre(t, true))
    }

    /**
     * AVL tree test -- Insert: Fall 1' (u weiter oben)
     */
    @Test
    fun testInsertCase1SymmetricComplex() {
        val t = getImplementation()
        t.put(7, "7")
        t.put(4, "4")
        t.put(11, "11")
        t.put(2, "2")
        t.put(5, "5")
        t.put(8, "8")
        t.put(13, "13")
        t.put(1, "1")
        t.put(3, "3")
        t.put(6, "6")
        //tree before insertion
        assertEquals("(1,1,0) (2,2,1) (3,3,0) (4,4,2) (5,5,1) (6,6,0) (7,7,3) (8,8,0) (11,11,1) (13,13,0)", toString(t, true))
        assertEquals("(7,7,3) (4,4,2) (2,2,1) (1,1,0) (3,3,0) (5,5,1) (6,6,0) (11,11,1) (8,8,0) (13,13,0)", toStringPre(t, true))
        // nochmal Fall 1' aber mit u weiter oben:
        // u = 7, balance(u) = -2, v = 4 , w = 2, v = b
        t.put(0, "0")
        //tree after insertion
        assertEquals("(0,0,0) (1,1,1) (2,2,2) (3,3,0) (4,4,3) (5,5,1) (6,6,0) (7,7,2) (8,8,0) (11,11,1) (13,13,0)", toString(t, true))
        assertEquals("(4,4,3) (2,2,2) (1,1,1) (0,0,0) (3,3,0) (7,7,2) (5,5,1) (6,6,0) (11,11,1) (8,8,0) (13,13,0)", toStringPre(t, true))
    }

    /**
     * AVL tree test -- Insert: consecutive operations
     */
    @Test
    fun testInsertChain() {
        val t = getImplementation()
        t.put(7, "7")
        t.put(4, "4")
        t.put(3, "3") // Fall 1'
        t.put(11, "11")
        t.put(13, "13") // Fall 1
        t.put(8, "8") // Fall 2
        t.put(5, "5")
        t.put(1, "1")
        t.put(2, "2") // Fall 2'
        t.put(6, "6")
        t.put(0, "0") // nochmal Fall 1' (u weiter oben)
        assertEquals("(0,0,0) (1,1,1) (2,2,2) (3,3,0) (4,4,3) (5,5,1) (6,6,0) (7,7,2) (8,8,0) (11,11,1) (13,13,0)", toString(t, true))
        assertEquals("(4,4,3) (2,2,2) (1,1,1) (0,0,0) (3,3,0) (7,7,2) (5,5,1) (6,6,0) (11,11,1) (8,8,0) (13,13,0)", toStringPre(t, true))
    }

    /**
     * AVL tree test -- Delete: Fall 1
     */
    @Test
    fun testDeleteCase1() {
        val t = getImplementation()
        t.put(4, "4")
        t.put(3, "3")
        t.put(11, "11")
        t.put(7, "7")
        t.put(13, "13")
        //Tree before deletion
        assertEquals("(3,3,0) (4,4,2) (7,7,0) (11,11,1) (13,13,0)", toString(t, true))
        assertEquals("(4,4,2) (3,3,0) (11,11,1) (7,7,0) (13,13,0)", toStringPre(t, true))
        // Fall 1: u =  4, balance(u) = 2, v = 11, w = 13, v = b
        assertEquals("3", t.remove(3))
        //Tree after deletion
        assertEquals("(4,4,1) (7,7,0) (11,11,2) (13,13,0)", toString(t, true))
        assertEquals("(11,11,2) (4,4,1) (7,7,0) (13,13,0)", toStringPre(t, true))
    }

    /**
     * AVL tree test -- Delete: Fall 2'
     */
    @Test
    fun testDeleteCase2Symmetric() {
        val t = getImplementation()
        t.put(6, "6")
        t.put(2, "2")
        t.put(8, "8")
        t.put(1, "1")
        t.put(4, "4")
        t.put(11, "11")
        t.put(3, "3")
        //Tree before deletion
        assertEquals("(1,1,0) (2,2,2) (3,3,0) (4,4,1) (6,6,3) (8,8,1) (11,11,0)", toString(t, true))
        assertEquals("(6,6,3) (2,2,2) (1,1,0) (4,4,1) (3,3,0) (8,8,1) (11,11,0)", toStringPre(t, true))
        // Fall 2': u =  6, balance(u) = -2, v = 2, w = 4, w = b
        t.remove(8)
        //Tree after deletion
        assertEquals("(1,1,0) (2,2,1) (3,3,0) (4,4,2) (6,6,1) (11,11,0)", toString(t, true))
        assertEquals("(4,4,2) (2,2,1) (1,1,0) (3,3,0) (6,6,1) (11,11,0)", toStringPre(t, true))
    }

    /**
     * AVL tree test -- Delete: Zwei Rotationen
     */
    @Test
    fun testDeleteTwoRotations() {
        val t = getImplementation()
        t.put(5, "5")
        t.put(3, "3")
        t.put(8, "8")
        t.put(2, "2")
        t.put(4, "4")
        t.put(7, "7")
        t.put(10, "10")
        t.put(1, "1")
        t.put(6, "6")
        t.put(9, "9")
        t.put(12, "12")
        t.put(11, "11")
        //Tree before deletion
        assertEquals("(1,1,0) (2,2,1) (3,3,2) (4,4,0) (5,5,4) (6,6,0) (7,7,1) (8,8,3) (9,9,0) (10,10,2) (11,11,0) (12,12,1)", toString(t, true))
        assertEquals("(5,5,4) (3,3,2) (2,2,1) (1,1,0) (4,4,0) (8,8,3) (7,7,1) (6,6,0) (10,10,2) (9,9,0) (12,12,1) (11,11,0)", toStringPre(t, true))
        // Fall 1': u =  3, balance(u) = -2, v = 2, w = 1, v = b
        // gefolgt von
        // Fall 1: u = 5, balance(u) = 2, v = 8, w = 10, v = b
        t.remove(4)
        //Tree after deletion
        assertEquals("(1,1,0) (2,2,1) (3,3,0) (5,5,2) (6,6,0) (7,7,1) (8,8,3) (9,9,0) (10,10,2) (11,11,0) (12,12,1)", toString(t, true))
        assertEquals("(8,8,3) (5,5,2) (2,2,1) (1,1,0) (3,3,0) (7,7,1) (6,6,0) (10,10,2) (9,9,0) (12,12,1) (11,11,0)", toStringPre(t, true))
    }

    /**
     * return a list containing the nodes of this binary tree in in-order,
     * e.g., a [LinkedList].
     *
     * @return list containing tree elements in in-order, empty list if tree is empty.
     */
    fun inorder(tree: BinaryTree<*, *>): List<BinaryTree.Node<*, *>> {
        val l = LinkedList<BinaryTree.Node<*, *>>()
        inorderTraversal(l, tree.root)
        return l
    }

    protected fun inorderTraversal(l: MutableList<BinaryTree.Node<*, *>>, v: BinaryTree.Node<*, *>?) {
        if (v != null) {
            inorderTraversal(l, v.leftChild)
            l.add(v)
            inorderTraversal(l, v.rightChild)
        }
    }

    /**
     * return a list containing the nodes of this binary tree in pre-order,
     * e.g., a [LinkedList].
     *
     * @return list containing tree elements in in-order, empty list if tree is empty.
     */
    fun preorder(tree: BinaryTree<*, *>): List<BinaryTree.Node<*, *>> {
        val l = LinkedList<BinaryTree.Node<*, *>>()
        preorderTraversal(l, tree.root)
        return l
    }

    protected fun preorderTraversal(l: MutableList<BinaryTree.Node<*, *>>, v: BinaryTree.Node<*, *>?) {
        if (v != null) {
            l.add(v)
            preorderTraversal(l, v.leftChild)
            preorderTraversal(l, v.rightChild)
        }
    }

    fun toString(tree: BinaryTree<*, *>, doOutputHeight: Boolean): String {
        val s = StringBuffer("")
        for (v in inorder(tree)) {
            if (doOutputHeight)
                s.append(toStringWithHeight(v) + " ")
            else
                s.append(toString(v) + " ")
        }
        return s.toString().trim { it <= ' ' }
    }

    fun toStringPre(tree: BinaryTree<*, *>, doOutputHeight: Boolean): String {
        val s = StringBuffer("")
        for (v in preorder(tree)) {
            if (doOutputHeight)
                s.append(toStringWithHeight(v) + " ")
            else
                s.append(toString(v) + " ")
        }
        return s.toString().trim { it <= ' ' }
    }

    fun toStringWithHeight(node: BinaryTree.Node<*, *>): String {
        return "(" + node.key.toString() + "," + node.value.toString() + "," + node.height + ")"
    }

    fun toString(node: BinaryTree.Node<*, *>): String {
        return "(" + node.key.toString() + "," + node.value.toString() + ")"
    }
}
