package de.jbamberger.algorithms.find

import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*
import java.util.stream.Collectors

/**
 * Taken from lecture "Algorithmen und Datenstrukturen", WS 2016 and then converted to Kotlin.
 *
 * @author Martin Mader, Daniel Kaiser
 * @author Jannik Bamberger (dev.jbamberger@gmail.com)
 */


class BinarySearchTreeTestImpl : BinarySearchTreeTest() {
    override fun <K : Comparable<K>, V> getImplementation(): BinaryTree<K, V> {
        return BinarySearchTree()
    }
}

abstract class BinarySearchTreeTest {

    abstract fun <K : Comparable<K>, V> getImplementation(): BinaryTree<K, V>

    @Test
    fun insertSimpleTest() {
        val t = getImplementation<Int, Any>()
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
        assertEquals("(1,1)", t.root!!.leftChild!!.asString())
        // left child of 1 should be null
        assertEquals(null, t.root!!.leftChild!!.leftChild)
        // right child of 1 should be 4
        assertEquals("(4,4)", t.root!!.leftChild!!.rightChild!!.asString())
        //test inorder
        assertEquals("(1,1) (4,4) (6,6) (7,7) (8,8) (9,9) (10,10) (11,11) (13,13)", t.asString(false))
        //test preorder
        assertEquals("(6,6) (1,1) (4,4) (11,11) (8,8) (7,7) (10,10) (9,9) (13,13)", t.asStringPre(false))
        //test size
        assertEquals(9, t.size())
        //insert an existing key (should return null and insert nothing)
        assertEquals(null, t.insert(7, "TEST"))
        //test inorder
        assertEquals("(1,1) (4,4) (6,6) (7,7) (8,8) (9,9) (10,10) (11,11) (13,13)", t.asString( false))
        //test preorder
        assertEquals("(6,6) (1,1) (4,4) (11,11) (8,8) (7,7) (10,10) (9,9) (13,13)", t.asStringPre(false))
    }

    /**
     * tests: - insert() with checking the height. The height is checked while doing the inorder an
     * preorder tests. - inserting the same key twice - size()
     */
    @Test
    fun insertHeightTest() {
        val t = getImplementation<Int, Any>()
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
        assertEquals("(1,1)", t.root!!.leftChild!!.asString())
        // left child of 1 should be null
        assertEquals(null, t.root!!.leftChild!!.leftChild)
        // right child of 1 should be 4
        assertEquals("(4,4)", t.root!!.leftChild!!.rightChild!!.asString())
        //test inorder + height check
        assertEquals("(1,1,1) (4,4,0) (6,6,4) (7,7,0) (8,8,2) (9,9,0) (10,10,1) (11,11,3) (13,13,0)", t.asString(true))
        //test preorder + height check
        assertEquals("(6,6,4) (1,1,1) (4,4,0) (11,11,3) (8,8,2) (7,7,0) (10,10,1) (9,9,0) (13,13,0)", t.asStringPre(true))
        //test size
        assertEquals(9, t.size())
        //insert an existing key (should return null and insert nothing)
        assertEquals(null, t.insert(7, "TEST"))
        //test inorder + height check
        assertEquals("(1,1,1) (4,4,0) (6,6,4) (7,7,0) (8,8,2) (9,9,0) (10,10,1) (11,11,3) (13,13,0)", t.asString(true))
        //test preorder + height check
        assertEquals("(6,6,4) (1,1,1) (4,4,0) (11,11,3) (8,8,2) (7,7,0) (10,10,1) (9,9,0) (13,13,0)", t.asStringPre(true))
    }


    /**
     * tests:
     * - delete without checking the hight
     * - size()
     */
    @Test
    fun deleteSimpleTest() {
        val t = getImplementation<Int, Any>()
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
        assertEquals("(10,10)", t.root!!.rightChild!!.asString())
        // ... left child of 10 should be 8
        assertEquals("(8,8)", t.root!!.rightChild!!.leftChild!!.asString())
        // ... right child of 8 should be 9
        assertEquals("(9,9)", t.root!!.rightChild!!.leftChild!!.rightChild!!.asString())
        // parent update check: parent of 9 should be 8
        assertEquals("(8,8)", t.root!!.rightChild!!.leftChild!!.rightChild!!.parent!!.asString())
        //test inorder
        assertEquals("(1,1) (4,4) (6,6) (7,7) (8,8) (9,9) (10,10) (13,13)", t.asString(false))
        //test preorder
        assertEquals("(6,6) (1,1) (4,4) (10,10) (8,8) (7,7) (9,9) (13,13)", t.asStringPre(false))
        //****************
        //delete leaf node
        //****************
        t.delete(9)
        // left child of 8 should be 7
        assertEquals("(7,7)", t.root!!.rightChild!!.leftChild!!.leftChild!!.asString())
        //  right child should be null
        Assert.assertEquals(null, t.root!!.rightChild!!.leftChild!!.rightChild)
        //test inorder
        assertEquals("(1,1) (4,4) (6,6) (7,7) (8,8) (10,10) (13,13)", t.asString(false))
        //test preorder
        assertEquals("(6,6) (1,1) (4,4) (10,10) (8,8) (7,7) (13,13)", t.asStringPre(false))
        //************************************************************************************
        //delete an inner node having both subtrees, but largest node is child of deleted node
        //************************************************************************************
        t.delete(10)
        // right child of root should be 8
        assertEquals("(8,8)", t.root!!.rightChild!!.asString())
        //left child of 8 should be 7
        assertEquals("(7,7)", t.root!!.rightChild!!.leftChild!!.asString())
        //right child of 8 should be 13
        assertEquals("(13,13)", t.root!!.rightChild!!.rightChild!!.asString())
        // parent check: parent of 7 should be 8
        assertEquals("(8,8)", t.root!!.rightChild!!.leftChild!!.parent!!.asString())
        //test inorder
        assertEquals("(1,1) (4,4) (6,6) (7,7) (8,8) (13,13)", t.asString(false))
        //test preorder
        assertEquals("(6,6) (1,1) (4,4) (8,8) (7,7) (13,13)", t.asStringPre(false))
        //******************************************
        // delete an inner node with no left subtree
        //******************************************
        t.delete(1)
        // left child of root should be 4
        assertEquals("(4,4)", t.root!!.leftChild!!.asString())
        // parent of 4 should be root (6)
        assertEquals("(6,6)", t.root!!.leftChild!!.parent!!.asString())
        assertEquals("(6,6)", t.root!!.asString())
        //test inorder
        assertEquals("(4,4) (6,6) (7,7) (8,8) (13,13)", t.asString(false))
        //test preorder
        assertEquals("(6,6) (4,4) (8,8) (7,7) (13,13)", t.asStringPre(false))
        //*********
        // delete 4
        //*********
        t.delete(4)
        // left child of root should be null
        Assert.assertEquals(null, t.root!!.leftChild)
        //test inorder
        assertEquals("(6,6) (7,7) (8,8) (13,13)", t.asString(false))
        //test preorder
        assertEquals("(6,6) (8,8) (7,7) (13,13)", t.asStringPre(false))
        //******************************************
        // delete root with no left subtree
        //******************************************
        t.delete(6)
        // root should be 8
        assertEquals("(8,8)", t.root!!.asString())
        //test inorder
        assertEquals("(7,7) (8,8) (13,13)", t.asString(false))
        //test preorder
        assertEquals("(8,8) (7,7) (13,13)", t.asStringPre(false))
        //******************************************
        // in between size test
        //******************************************
        // isEmpty must return false
        assertEquals(false, t.size() == 0)
        // size must be 3
        assertEquals(3, t.size())
        //******************************************
        //  delete everything except root
        //******************************************
        t.delete(7)
        t.delete(13)
        //test inorder
        assertEquals("(8,8)", t.asString(false))
        //test preorder
        assertEquals("(8,8)", t.asStringPre(false))
        //******************************************
        //  delete last node
        //******************************************
        t.delete(8)
        // root should be null
        Assert.assertEquals(null, t.root)
        // isEmpty() should return true
        assertEquals(true, t.size() == 0)
        // the size should be 0
        assertEquals(0, t.size())
        //test inorder
        assertEquals("", t.asString(false))
        //test preorder
        assertEquals("", t.asStringPre(false))
    }

    /**
     * tests: - delete with checking the height. The height is checked while doing the inorder an
     * preorder tests. - size()
     */
    @Test
    fun deleteHeightTest() {
        val t = getImplementation<Int, Any>()
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
        assertEquals("(10,10)", t.root!!.rightChild!!.asString())
        // ... left child of 10 should be 8
        assertEquals("(8,8)", t.root!!.rightChild!!.leftChild!!.asString())
        // ... right child of 8 should be 9
        assertEquals("(9,9)", t.root!!.rightChild!!.leftChild!!.rightChild!!.asString())
        // parent update check: parent of 9 should be 8
        assertEquals("(8,8)", t.root!!.rightChild!!.leftChild!!.rightChild!!.parent!!.asString())
        //test inorder + height check
        assertEquals("(1,1,1) (4,4,0) (6,6,3) (7,7,0) (8,8,1) (9,9,0) (10,10,2) (13,13,0)", t.asString( true))
        //test preorder + height check
        assertEquals("(6,6,3) (1,1,1) (4,4,0) (10,10,2) (8,8,1) (7,7,0) (9,9,0) (13,13,0)", t.asStringPre( true))
        //****************
        //delete leaf node
        //****************
        t.delete(9)
        // left child of 8 should be 7
        assertEquals("(7,7)", t.root!!.rightChild!!.leftChild!!.leftChild!!.asString())
        //  right child should be null
        Assert.assertEquals(null, t.root!!.rightChild!!.leftChild!!.rightChild)
        //test inorder + height check
        assertEquals("(1,1,1) (4,4,0) (6,6,3) (7,7,0) (8,8,1) (10,10,2) (13,13,0)", t.asString( true))
        //test preorder + height check
        assertEquals("(6,6,3) (1,1,1) (4,4,0) (10,10,2) (8,8,1) (7,7,0) (13,13,0)", t.asStringPre(true))
        //************************************************************************************
        //delete an inner node having both subtrees, but largest node is child of deleted node
        //************************************************************************************
        t.delete(10)
        // right child of root should be 8
        assertEquals("(8,8)", t.root!!.rightChild!!.asString())
        //left child of 8 should be 7
        assertEquals("(7,7)", t.root!!.rightChild!!.leftChild!!.asString())
        //right child of 8 should be 13
        assertEquals("(13,13)", t.root!!.rightChild!!.rightChild!!.asString())
        // parent check: parent of 7 should be 8
        assertEquals("(8,8)", t.root!!.rightChild!!.leftChild!!.parent!!.asString())
        //test inorder + height check
        assertEquals("(1,1,1) (4,4,0) (6,6,2) (7,7,0) (8,8,1) (13,13,0)", t.asString( true))
        //test preorder + height check
        assertEquals("(6,6,2) (1,1,1) (4,4,0) (8,8,1) (7,7,0) (13,13,0)", t.asStringPre(true))
        //******************************************
        // delete an inner node with no left subtree
        //******************************************
        t.delete(1)
        // left child of root should be 4
        assertEquals("(4,4)", t.root!!.leftChild!!.asString())
        // parent of 4 should be root (6)
        assertEquals("(6,6)", t.root!!.leftChild!!.parent!!.asString())
        assertEquals("(6,6)", t.root!!.asString())
        //test inorder + height check
        assertEquals("(4,4,0) (6,6,2) (7,7,0) (8,8,1) (13,13,0)", t.asString(true))
        //test preorder + height check
        assertEquals("(6,6,2) (4,4,0) (8,8,1) (7,7,0) (13,13,0)", t.asStringPre(true))
        //*********
        // delete 4
        //*********
        t.delete(4)
        // left child of root should be null
        Assert.assertEquals(null, t.root!!.leftChild)
        //test inorder + height check
        assertEquals("(6,6,2) (7,7,0) (8,8,1) (13,13,0)", t.asString(true))
        //test preorder + height check
        assertEquals("(6,6,2) (8,8,1) (7,7,0) (13,13,0)", t.asStringPre(true))
        //******************************************
        // delete root with no left subtree
        //******************************************
        t.delete(6)
        // root should be 8
        assertEquals("(8,8)", t.root!!.asString())
        //test inorder + height check
        assertEquals("(7,7,0) (8,8,1) (13,13,0)", t.asString(true))
        //test preorder + height check
        assertEquals("(8,8,1) (7,7,0) (13,13,0)", t.asStringPre(true))
        //******************************************
        // in between size test
        //******************************************
        // isEmpty must return false
        assertEquals(false, t.size() == 0)
        // size must be 3
        assertEquals(3, t.size())
        //******************************************
        //  delete everything except root
        //******************************************
        t.delete(7)
        t.delete(13)
        //test inorder + height check
        assertEquals("(8,8,0)", t.asString(true))
        //test preorder + height check
        assertEquals("(8,8,0)", t.asStringPre(true))
        //******************************************
        //  delete last node
        //******************************************
        t.delete(8)
        // root should be null
        Assert.assertEquals(null, t.root)
        // isEmpty() should return true
        assertEquals(true, t.size() == 0)
        // the size should be 0
        assertEquals(0, t.size())
        //test inorder
        assertEquals("", t.asString(true))
        //test preorder
        assertEquals("", t.asStringPre(true))
    }

    /**
     * tests findALL
     */
    @Test
    fun testFindAll() {
        val t = getImplementation<Int, Any>()
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
        // find a node (11), which is no leaf
        assertEquals("[11]", t.findInterval(9, 12).collect(Collectors.toList()).toString())
        // all keys larger than the search area
        assertEquals("[]", t.findInterval(-5, -1).collect(Collectors.toList()).toString())
        // all keys smaller than the search area
        assertEquals("[]", t.findInterval(14, 17).collect(Collectors.toList()).toString())

        // test with string keys
        val t2 = getImplementation<String, Any>()
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
    private fun <K : Comparable<K>, V> BinaryTree<K, V>.inorder(): List<BinaryTree.Node<K, V>> {
        val l = LinkedList<BinaryTree.Node<K, V>>()

        fun inorderTraversal(v: BinaryTree.Node<K, V>?) {
            if (v == null) return
            inorderTraversal(v.leftChild)
            l.add(v)
            inorderTraversal(v.rightChild)
        }

        inorderTraversal(this.root)
        return l
    }

    /**
     * return a list containing the nodes of this binary tree in pre-order,
     * e.g., a [LinkedList].
     *
     * @return list containing tree elements in in-order, empty list if tree is empty.
     */
    private fun <K : Comparable<K>, V> BinaryTree<K, V>.preorder(): List<BinaryTree.Node<K, V>> {
        val l = LinkedList<BinaryTree.Node<K, V>>()

        fun preorderTraversal(v: BinaryTree.Node<K, V>?) {
            if (v == null) return
            l.add(v)
            preorderTraversal(v.leftChild)
            preorderTraversal(v.rightChild)
        }

        preorderTraversal(this.root)
        return l
    }

    private fun <K : Comparable<K>, V> BinaryTree<K, V>.asString(doOutputHeight: Boolean): String {
        return when{
            doOutputHeight -> this.inorder().joinToString(" ", transform = { it.asStringWithHeight() })
            else -> this.inorder().joinToString(" ", transform = { it.asString() })
        }
    }

    private fun <K : Comparable<K>, V> BinaryTree<K, V>.asStringPre(doOutputHeight: Boolean): String {
        return when{
            doOutputHeight -> this.preorder().joinToString(" ", transform = { it.asStringWithHeight() })
            else -> this.preorder().joinToString(" ", transform = { it.asString() })
        }
    }

    private fun <K : Comparable<K>, V> BinaryTree.Node<K, V>.asStringWithHeight(): String {
        return "(" + this.key.toString() + "," + this.value.toString() + "," + this.height + ")"
    }

    private fun <K : Comparable<K>, V> BinaryTree.Node<K, V>.asString(): String {
        return "(" + this.key.toString() + "," + this.value.toString() + ")"
    }
}