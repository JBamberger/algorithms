package de.jbamberger.algorithms.map

import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*
import java.util.stream.Collectors

/**
 * Inspired by test cases provide for the lecture "Algorithmen und Datenstrukturen" held in the winter term 2016/17 at
 * the University of Konstanz.
 */
abstract class BinarySearchTreeTest {

    /**
     * @return an instance of the class to be tested.
     */
    abstract fun <K : Comparable<K>, V> getImplementation(): BinaryTree<K, V>


    /**
     * tests:
     * - insert() without checking the height
     * - inserting the same key twice
     * - size()
     */
    @Test
    fun insertSimpleTest() {
        val t = getImplementation<Int, String>()
        //insert some nodes
        assertEquals("6", t.insert(6, "6"))  //test if insert returns the inserted Element
        t.insert(1, "1")
        t.insert(4, "4")
        t.insert(11, "11")
        t.insert(8, "8")
        t.insert(7, "7")
        t.insert(13, "13")
        t.insert(10, "10")
        t.insert(9, "9")
        // left child of root should be 1
        assertEquals("(1,1)", toString(t.getRoot()!!.getLeftChild()))
        // left child of 1 should be null
        assertEquals(null, t.getRoot()!!.getLeftChild()!!.getLeftChild())
        // right child of 1 should be 4
        assertEquals("(4,4)", toString(t.getRoot()!!.getLeftChild()!!.getRightChild()))
        //test inorder
        assertEquals("(1,1) (4,4) (6,6) (7,7) (8,8) (9,9) (10,10) (11,11) (13,13)", toString(t, false))
        //test preorder
        assertEquals("(6,6) (1,1) (4,4) (11,11) (8,8) (7,7) (10,10) (9,9) (13,13)", toStringPre(t, false))
        //test size
        assertEquals(9, t.size().toLong())
        //insert an existing key (should return null and insert nothing)
        assertEquals(null, t.insert(7, "TEST"))
        //test inorder
        assertEquals("(1,1) (4,4) (6,6) (7,7) (8,8) (9,9) (10,10) (11,11) (13,13)", toString(t, false))
        //test preorder
        assertEquals("(6,6) (1,1) (4,4) (11,11) (8,8) (7,7) (10,10) (9,9) (13,13)", toStringPre(t, false))
    }

    /**
     * tests: - insert() with checking the height. The height is checked while doing the inorder an
     * preorder tests. - inserting the same key twice - size()
     */
    @Test
    fun insertHeightTest() {
        val t = getImplementation<Int, String>()
        //insert some nodes
        assertEquals("6", t.insert(6, "6"))  //test if insert returns the inserted Element
        t.insert(1, "1")
        t.insert(4, "4")
        t.insert(11, "11")
        t.insert(8, "8")
        t.insert(7, "7")
        t.insert(13, "13")
        t.insert(10, "10")
        t.insert(9, "9")
        // left child of root should be 1
        assertEquals("(1,1)", toString(t.getRoot()!!.getLeftChild()))
        // left child of 1 should be null
        assertEquals(null, t.getRoot()!!.getLeftChild()!!.getLeftChild())
        // right child of 1 should be 4
        assertEquals("(4,4)", toString(t.getRoot()!!.getLeftChild()!!.getRightChild()))
        //test inorder + height check
        assertEquals("(1,1,1) (4,4,0) (6,6,4) (7,7,0) (8,8,2) (9,9,0) (10,10,1) (11,11,3) (13,13,0)", toString(t, true))
        //test preorder + height check
        assertEquals("(6,6,4) (1,1,1) (4,4,0) (11,11,3) (8,8,2) (7,7,0) (10,10,1) (9,9,0) (13,13,0)", toStringPre(t, true))
        //test size
        assertEquals(9, t.size().toLong())
        //insert an existing key (should return null and insert nothing)
        assertEquals(null, t.insert(7, "TEST"))
        //test inorder + height check
        assertEquals("(1,1,1) (4,4,0) (6,6,4) (7,7,0) (8,8,2) (9,9,0) (10,10,1) (11,11,3) (13,13,0)", toString(t, true))
        //test preorder + height check
        assertEquals("(6,6,4) (1,1,1) (4,4,0) (11,11,3) (8,8,2) (7,7,0) (10,10,1) (9,9,0) (13,13,0)", toStringPre(t, true))
    }


    /**
     * tests:
     * - delete without checking the hight
     * - size()
     */
    @Test
    fun deleteSimpleTest() {
        val t = getImplementation<Int, String>()
        t.insert(6, "6")
        t.insert(1, "1")
        t.insert(4, "4")
        t.insert(11, "11")
        t.insert(8, "8")
        t.insert(7, "7")
        t.insert(13, "13")
        t.insert(10, "10")
        t.insert(9, "9")
        //*************************************************************
        //delete an inner node with having both left and right subtree
        //*************************************************************
        assertEquals("11", t.delete(11))    //test if delete returns the deleted Element
        //after deleting 11 ...
        // ... right child of root should be 10
        assertEquals("(10,10)", toString(t.getRoot()!!.getRightChild()))
        // ... left child of 10 should be 8
        assertEquals("(8,8)", toString(t.getRoot()!!.getRightChild()!!.getLeftChild()))
        // ... right child of 8 should be 9
        assertEquals("(9,9)", toString(t.getRoot()!!.getRightChild()!!.getLeftChild()!!.getRightChild()))
        // parent update check: parent of 9 should be 8
        assertEquals("(8,8)", toString(t.getRoot()!!.getRightChild()!!.getLeftChild()!!.getRightChild()!!.getParent()))
        //test inorder
        assertEquals("(1,1) (4,4) (6,6) (7,7) (8,8) (9,9) (10,10) (13,13)", toString(t, false))
        //test preorder
        assertEquals("(6,6) (1,1) (4,4) (10,10) (8,8) (7,7) (9,9) (13,13)", toStringPre(t, false))
        //****************
        //delete leaf node
        //****************
        t.delete(9)
        // left child of 8 should be 7
        assertEquals("(7,7)", toString(t.getRoot()!!.getRightChild()!!.getLeftChild()!!.getLeftChild()))
        //  right child should be null
        Assert.assertEquals(null, t.getRoot()!!.getRightChild()!!.getLeftChild()!!.getRightChild())
        //test inorder
        assertEquals("(1,1) (4,4) (6,6) (7,7) (8,8) (10,10) (13,13)", toString(t, false))
        //test preorder
        assertEquals("(6,6) (1,1) (4,4) (10,10) (8,8) (7,7) (13,13)", toStringPre(t, false))
        //************************************************************************************
        //delete an inner node having both subtrees, but largest node is child of deleted node
        //************************************************************************************
        t.delete(10)
        // right child of root should be 8
        assertEquals("(8,8)", toString(t.getRoot()!!.getRightChild()))
        //left child of 8 should be 7
        assertEquals("(7,7)", toString(t.getRoot()!!.getRightChild()!!.getLeftChild()))
        //right child of 8 should be 13
        assertEquals("(13,13)", toString(t.getRoot()!!.getRightChild()!!.getRightChild()))
        // parent check: parent of 7 should be 8
        assertEquals("(8,8)", toString(t.getRoot()!!.getRightChild()!!.getLeftChild()!!.getParent()))
        //test inorder
        assertEquals("(1,1) (4,4) (6,6) (7,7) (8,8) (13,13)", toString(t, false))
        //test preorder
        assertEquals("(6,6) (1,1) (4,4) (8,8) (7,7) (13,13)", toStringPre(t, false))
        //******************************************
        // delete an inner node with no left subtree
        //******************************************
        t.delete(1)
        // left child of root should be 4
        assertEquals("(4,4)", toString(t.getRoot()!!.getLeftChild()))
        // parent of 4 should be root (6)
        assertEquals("(6,6)", toString(t.getRoot()!!.getLeftChild()!!.getParent()))
        assertEquals("(6,6)", toString(t.getRoot()))
        //test inorder
        assertEquals("(4,4) (6,6) (7,7) (8,8) (13,13)", toString(t, false))
        //test preorder
        assertEquals("(6,6) (4,4) (8,8) (7,7) (13,13)", toStringPre(t, false))
        //*********
        // delete 4
        //*********
        t.delete(4)
        // left child of root should be null
        Assert.assertEquals(null, t.getRoot()!!.getLeftChild())
        //test inorder
        assertEquals("(6,6) (7,7) (8,8) (13,13)", toString(t, false))
        //test preorder
        assertEquals("(6,6) (8,8) (7,7) (13,13)", toStringPre(t, false))
        //******************************************
        // delete root with no left subtree
        //******************************************
        t.delete(6)
        // root should be 8
        assertEquals("(8,8)", toString(t.getRoot()))
        //test inorder
        assertEquals("(7,7) (8,8) (13,13)", toString(t, false))
        //test preorder
        assertEquals("(8,8) (7,7) (13,13)", toStringPre(t, false))
        //******************************************
        // in between size test
        //******************************************
        // isEmpty must return false
        assertEquals(false, t.size() == 0)
        // size must be 3
        assertEquals(3, t.size().toLong())
        //******************************************
        //  delete everything except root
        //******************************************
        t.delete(7)
        t.delete(13)
        //test inorder
        assertEquals("(8,8)", toString(t, false))
        //test preorder
        assertEquals("(8,8)", toStringPre(t, false))
        //******************************************
        //  delete last node
        //******************************************
        t.delete(8)
        // root should be null
        Assert.assertEquals(null, t.getRoot())
        // isEmpty() should return true
        assertEquals(true, t.size() == 0)
        // the size should be 0
        assertEquals(0, t.size().toLong())
        //test inorder
        assertEquals("", toString(t, false))
        //test preorder
        assertEquals("", toStringPre(t, false))
    }

    /**
     * tests: - delete with checking the height. The height is checked while doing the inorder an
     * preorder tests. - size()
     */
    @Test
    fun deleteHeightTest() {
        val t = getImplementation<Int, String>()
        t.insert(6, "6")
        t.insert(1, "1")
        t.insert(4, "4")
        t.insert(11, "11")
        t.insert(8, "8")
        t.insert(7, "7")
        t.insert(13, "13")
        t.insert(10, "10")
        t.insert(9, "9")
        //*************************************************************
        //delete an inner node with having both left and right subtree
        //*************************************************************
        assertEquals("11", t.delete(11))    //test if delete returns the deleted Element
        //after deleting 11 ...
        // ... right child of root should be 10
        assertEquals("(10,10)", toString(t.getRoot()!!.getRightChild()))
        // ... left child of 10 should be 8
        assertEquals("(8,8)", toString(t.getRoot()!!.getRightChild()!!.getLeftChild()))
        // ... right child of 8 should be 9
        assertEquals("(9,9)", toString(t.getRoot()!!.getRightChild()!!.getLeftChild()!!.getRightChild()))
        // parent update check: parent of 9 should be 8
        assertEquals("(8,8)", toString(t.getRoot()!!.getRightChild()!!.getLeftChild()!!.getRightChild()!!.getParent()))
        //test inorder + height check
        assertEquals("(1,1,1) (4,4,0) (6,6,3) (7,7,0) (8,8,1) (9,9,0) (10,10,2) (13,13,0)", toString(t, true))
        //test preorder + height check
        assertEquals("(6,6,3) (1,1,1) (4,4,0) (10,10,2) (8,8,1) (7,7,0) (9,9,0) (13,13,0)", toStringPre(t, true))
        //****************
        //delete leaf node
        //****************
        t.delete(9)
        // left child of 8 should be 7
        assertEquals("(7,7)", toString(t.getRoot()!!.getRightChild()!!.getLeftChild()!!.getLeftChild()))
        //  right child should be null
        Assert.assertEquals(null, t.getRoot()!!.getRightChild()!!.getLeftChild()!!.getRightChild())
        //test inorder + height check
        assertEquals("(1,1,1) (4,4,0) (6,6,3) (7,7,0) (8,8,1) (10,10,2) (13,13,0)", toString(t, true))
        //test preorder + height check
        assertEquals("(6,6,3) (1,1,1) (4,4,0) (10,10,2) (8,8,1) (7,7,0) (13,13,0)", toStringPre(t, true))
        //************************************************************************************
        //delete an inner node having both subtrees, but largest node is child of deleted node
        //************************************************************************************
        t.delete(10)
        // right child of root should be 8
        assertEquals("(8,8)", toString(t.getRoot()!!.getRightChild()))
        //left child of 8 should be 7
        assertEquals("(7,7)", toString(t.getRoot()!!.getRightChild()!!.getLeftChild()))
        //right child of 8 should be 13
        assertEquals("(13,13)", toString(t.getRoot()!!.getRightChild()!!.getRightChild()))
        // parent check: parent of 7 should be 8
        assertEquals("(8,8)", toString(t.getRoot()!!.getRightChild()!!.getLeftChild()!!.getParent()))
        //test inorder + height check
        assertEquals("(1,1,1) (4,4,0) (6,6,2) (7,7,0) (8,8,1) (13,13,0)", toString(t, true))
        //test preorder + height check
        assertEquals("(6,6,2) (1,1,1) (4,4,0) (8,8,1) (7,7,0) (13,13,0)", toStringPre(t, true))
        //******************************************
        // delete an inner node with no left subtree
        //******************************************
        t.delete(1)
        // left child of root should be 4
        assertEquals("(4,4)", toString(t.getRoot()!!.getLeftChild()))
        // parent of 4 should be root (6)
        assertEquals("(6,6)", toString(t.getRoot()!!.getLeftChild()!!.getParent()))
        assertEquals("(6,6)", toString(t.getRoot()))
        //test inorder + height check
        assertEquals("(4,4,0) (6,6,2) (7,7,0) (8,8,1) (13,13,0)", toString(t, true))
        //test preorder + height check
        assertEquals("(6,6,2) (4,4,0) (8,8,1) (7,7,0) (13,13,0)", toStringPre(t, true))
        //*********
        // delete 4
        //*********
        t.delete(4)
        // left child of root should be null
        Assert.assertEquals(null, t.getRoot()!!.getLeftChild())
        //test inorder + height check
        assertEquals("(6,6,2) (7,7,0) (8,8,1) (13,13,0)", toString(t, true))
        //test preorder + height check
        assertEquals("(6,6,2) (8,8,1) (7,7,0) (13,13,0)", toStringPre(t, true))
        //******************************************
        // delete root with no left subtree
        //******************************************
        t.delete(6)
        // root should be 8
        assertEquals("(8,8)", toString(t.getRoot()))
        //test inorder + height check
        assertEquals("(7,7,0) (8,8,1) (13,13,0)", toString(t, true))
        //test preorder + height check
        assertEquals("(8,8,1) (7,7,0) (13,13,0)", toStringPre(t, true))
        //******************************************
        // in between size test
        //******************************************
        // isEmpty must return false
        assertEquals(false, t.size() == 0)
        // size must be 3
        assertEquals(3, t.size().toLong())
        //******************************************
        //  delete everything except root
        //******************************************
        t.delete(7)
        t.delete(13)
        //test inorder + height check
        assertEquals("(8,8,0)", toString(t, true))
        //test preorder + height check
        assertEquals("(8,8,0)", toStringPre(t, true))
        //******************************************
        //  delete last node
        //******************************************
        t.delete(8)
        // root should be null
        Assert.assertEquals(null, t.getRoot())
        // isEmpty() should return true
        assertEquals(true, t.size() == 0)
        // the size should be 0
        assertEquals(0, t.size().toLong())
        //test inorder
        assertEquals("", toString(t, true))
        //test preorder
        assertEquals("", toStringPre(t, true))
    }

    /**
     * tests findALL
     */
    @Test
    fun testFindAll() {
        val t = getImplementation<Int, String>()
        t.insert(7, "7")
        t.insert(4, "4")
        t.insert(3, "3")
        t.insert(11, "11")
        t.insert(13, "13")
        t.insert(8, "8")
        t.insert(5, "5")
        t.insert(1, "1")
        t.insert(2, "2")
        t.insert(6, "6")
        t.insert(0, "0")
        assertEquals("[3, 4, 5, 6, 7]", t.findInterval(3, 7).collect(Collectors.toList()).toString())
        // findet einen Knoten (11), der kein Blatt ist
        assertEquals("[11]", t.findInterval(9, 12).collect(Collectors.toList()).toString())
        // alle Schlüssel größer als Suchbereich
        assertEquals("[]", t.findInterval(-5, -1).collect(Collectors.toList()).toString())
        // alle Schlüssel kleiner als Suchbereich
        assertEquals("[]", t.findInterval(14, 17).collect(Collectors.toList()).toString())
        /* test mit String als Schlüssel */
        val t2 = getImplementation<String, String>()
        t2.insert("a", "a")
        t2.insert("aa", "aa")
        t2.insert("ab", "ab")
        t2.insert("b", "b")
        t2.insert("ca", "ca")
        t2.insert("cac", "cac")
        t2.insert("x", "x")
        t2.insert("y", "y")
        t2.insert("aaba", "aaba")
        assertEquals("[aa, aaba, ab, b]", t2.findInterval("aa", "c").collect(Collectors.toList()).toString())
    }

    /**
     * return a list containing the nodes of this binary tree in in-order,
     * e.g., a [LinkedList].
     *
     * @return list containing tree elements in in-order, empty list if tree is empty.
     */
    fun inorder(tree: BinaryTree<*, *>): List<BinaryTree.Node<*, *>> {
        val l = LinkedList<BinaryTree.Node<*, *>>()
        inorderTraversal(l, tree.getRoot())
        return l
    }

    protected fun inorderTraversal(l: MutableList<BinaryTree.Node<*, *>>, v: BinaryTree.Node<*, *>?) {
        if (v != null) {
            inorderTraversal(l, v.getLeftChild())
            l.add(v)
            inorderTraversal(l, v.getRightChild())
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
        preorderTraversal(l, tree.getRoot())
        return l
    }

    protected fun preorderTraversal(l: MutableList<BinaryTree.Node<*, *>>, v: BinaryTree.Node<*, *>?) {
        if (v != null) {
            l.add(v)
            preorderTraversal(l, v.getLeftChild())
            preorderTraversal(l, v.getRightChild())
        }
    }

    fun toString(tree: BinaryTree<*, *>, doOutputHeight: Boolean): String {
        val s = StringBuffer("")
        for (v in inorder(tree)) {
            if (doOutputHeight) {
                s.append(toStringWithHeight(v) + " ")
            } else {
                s.append(toString(v) + " ")
            }
        }
        return s.toString().trim { it <= ' ' }
    }

    fun toStringPre(tree: BinaryTree<*, *>, doOutputHeight: Boolean): String {
        val s = StringBuffer("")
        for (v in preorder(tree)) {
            if (doOutputHeight) {
                s.append(toStringWithHeight(v) + " ")
            } else {
                s.append(toString(v) + " ")
            }
        }
        return s.toString().trim { it <= ' ' }
    }

    fun toStringWithHeight(node: BinaryTree.Node<*, *>): String {
        return "(" + node.getKey().toString() + "," + node.getValue().toString() + "," + node.getHeight() + ")"
    }

    fun toString(node: BinaryTree.Node<*, *>?): String {
        return "(" + node!!.getKey().toString() + "," + node.getValue().toString() + ")"
    }
}
