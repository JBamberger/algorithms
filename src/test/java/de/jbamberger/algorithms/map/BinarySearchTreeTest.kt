package de.jbamberger.algorithms.map

import de.jbamberger.algorithms.map.TestHelper.toString
import de.jbamberger.algorithms.map.TestHelper.toStringPre
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Inspired by test cases provide for the lecture "Algorithmen und Datenstrukturen" held in the winter term 2016/17 at
 * the University of Konstanz.
 */
abstract class BinarySearchTreeTest {

    abstract fun <K : Comparable<K>, V> getImplementation(): BinaryTree<K, V>


    /**
     * tests:
     * - put() without checking the height
     * - inserting the same key twice
     * - size()
     */
    @Test
    fun insertSimpleTest() {
        val t = getImplementation<Int, String>()
        //put some nodes
        assertEquals("6", t.put(6, "6"))  //test if put returns the inserted Element
        t.put(1, "1")
        t.put(4, "4")
        t.put(11, "11")
        t.put(8, "8")
        t.put(7, "7")
        t.put(13, "13")
        t.put(10, "10")
        t.put(9, "9")
        // left child of root should be 1
        assertEquals("(1,1)", toString(t.root!!.leftChild))
        // left child of 1 should be null
        assertEquals(null, t.root!!.leftChild!!.leftChild)
        // right child of 1 should be 4
        assertEquals("(4,4)", toString(t.root!!.leftChild!!.rightChild))
        //test inorder
        assertEquals("(1,1) (4,4) (6,6) (7,7) (8,8) (9,9) (10,10) (11,11) (13,13)", toString(t, false))
        //test preorder
        assertEquals("(6,6) (1,1) (4,4) (11,11) (8,8) (7,7) (10,10) (9,9) (13,13)", toStringPre(t, false))
        //test size
        assertEquals(9, t.size.toLong())
        //put an existing key (should return null and put nothing)
        assertEquals(null, t.put(7, "TEST"))
        //test inorder
        assertEquals("(1,1) (4,4) (6,6) (7,7) (8,8) (9,9) (10,10) (11,11) (13,13)", toString(t, false))
        //test preorder
        assertEquals("(6,6) (1,1) (4,4) (11,11) (8,8) (7,7) (10,10) (9,9) (13,13)", toStringPre(t, false))
    }

    /**
     * tests: - put() with checking the height. The height is checked while doing the inorder an
     * preorder tests. - inserting the same key twice - size()
     */
    @Test
    fun insertHeightTest() {
        val t = getImplementation<Int, String>()
        //put some nodes
        assertEquals("6", t.put(6, "6"))  //test if put returns the inserted Element
        t.put(1, "1")
        t.put(4, "4")
        t.put(11, "11")
        t.put(8, "8")
        t.put(7, "7")
        t.put(13, "13")
        t.put(10, "10")
        t.put(9, "9")
        // left child of root should be 1
        assertEquals("(1,1)", toString(t.root!!.leftChild))
        // left child of 1 should be null
        assertEquals(null, t.root!!.leftChild!!.leftChild)
        // right child of 1 should be 4
        assertEquals("(4,4)", toString(t.root!!.leftChild!!.rightChild))
        //test inorder + height check
        assertEquals("(1,1,1) (4,4,0) (6,6,4) (7,7,0) (8,8,2) (9,9,0) (10,10,1) (11,11,3) (13,13,0)", toString(t, true))
        //test preorder + height check
        assertEquals("(6,6,4) (1,1,1) (4,4,0) (11,11,3) (8,8,2) (7,7,0) (10,10,1) (9,9,0) (13,13,0)", toStringPre(t, true))
        //test size
        assertEquals(9, t.size.toLong())
        //put an existing key (should return null and put nothing)
        assertEquals(null, t.put(7, "TEST"))
        //test inorder + height check
        assertEquals("(1,1,1) (4,4,0) (6,6,4) (7,7,0) (8,8,2) (9,9,0) (10,10,1) (11,11,3) (13,13,0)", toString(t, true))
        //test preorder + height check
        assertEquals("(6,6,4) (1,1,1) (4,4,0) (11,11,3) (8,8,2) (7,7,0) (10,10,1) (9,9,0) (13,13,0)", toStringPre(t, true))
    }


    /**
     * tests:
     * - remove without checking the hight
     * - size()
     */
    @Test
    fun deleteSimpleTest() {
        val t = getImplementation<Int, String>()
        t.put(6, "6")
        t.put(1, "1")
        t.put(4, "4")
        t.put(11, "11")
        t.put(8, "8")
        t.put(7, "7")
        t.put(13, "13")
        t.put(10, "10")
        t.put(9, "9")
        //*************************************************************
        //remove an inner node with having both left and right subtree
        //*************************************************************
        assertEquals("11", t.remove(11))    //test if remove returns the deleted Element
        //after deleting 11 ...
        // ... right child of root should be 10
        assertEquals("(10,10)", toString(t.root!!.rightChild))
        // ... left child of 10 should be 8
        assertEquals("(8,8)", toString(t.root!!.rightChild!!.leftChild))
        // ... right child of 8 should be 9
        assertEquals("(9,9)", toString(t.root!!.rightChild!!.leftChild!!.rightChild))
        // parent update check: parent of 9 should be 8
        assertEquals("(8,8)", toString(t.root!!.rightChild!!.leftChild!!.rightChild!!.parent))
        //test inorder
        assertEquals("(1,1) (4,4) (6,6) (7,7) (8,8) (9,9) (10,10) (13,13)", toString(t, false))
        //test preorder
        assertEquals("(6,6) (1,1) (4,4) (10,10) (8,8) (7,7) (9,9) (13,13)", toStringPre(t, false))
        //****************
        //remove leaf node
        //****************
        t.remove(9)
        // left child of 8 should be 7
        assertEquals("(7,7)", toString(t.root!!.rightChild!!.leftChild!!.leftChild))
        //  right child should be null
        Assert.assertEquals(null, t.root!!.rightChild!!.leftChild!!.rightChild)
        //test inorder
        assertEquals("(1,1) (4,4) (6,6) (7,7) (8,8) (10,10) (13,13)", toString(t, false))
        //test preorder
        assertEquals("(6,6) (1,1) (4,4) (10,10) (8,8) (7,7) (13,13)", toStringPre(t, false))
        //************************************************************************************
        //remove an inner node having both subtrees, but largest node is child of deleted node
        //************************************************************************************
        t.remove(10)
        // right child of root should be 8
        assertEquals("(8,8)", toString(t.root!!.rightChild))
        //left child of 8 should be 7
        assertEquals("(7,7)", toString(t.root!!.rightChild!!.leftChild))
        //right child of 8 should be 13
        assertEquals("(13,13)", toString(t.root!!.rightChild!!.rightChild))
        // parent check: parent of 7 should be 8
        assertEquals("(8,8)", toString(t.root!!.rightChild!!.leftChild!!.parent))
        //test inorder
        assertEquals("(1,1) (4,4) (6,6) (7,7) (8,8) (13,13)", toString(t, false))
        //test preorder
        assertEquals("(6,6) (1,1) (4,4) (8,8) (7,7) (13,13)", toStringPre(t, false))
        //******************************************
        // remove an inner node with no left subtree
        //******************************************
        t.remove(1)
        // left child of root should be 4
        assertEquals("(4,4)", toString(t.root!!.leftChild))
        // parent of 4 should be root (6)
        assertEquals("(6,6)", toString(t.root!!.leftChild!!.parent))
        assertEquals("(6,6)", toString(t.root))
        //test inorder
        assertEquals("(4,4) (6,6) (7,7) (8,8) (13,13)", toString(t, false))
        //test preorder
        assertEquals("(6,6) (4,4) (8,8) (7,7) (13,13)", toStringPre(t, false))
        //*********
        // remove 4
        //*********
        t.remove(4)
        // left child of root should be null
        Assert.assertEquals(null, t.root!!.leftChild)
        //test inorder
        assertEquals("(6,6) (7,7) (8,8) (13,13)", toString(t, false))
        //test preorder
        assertEquals("(6,6) (8,8) (7,7) (13,13)", toStringPre(t, false))
        //******************************************
        // remove root with no left subtree
        //******************************************
        t.remove(6)
        // root should be 8
        assertEquals("(8,8)", toString(t.root))
        //test inorder
        assertEquals("(7,7) (8,8) (13,13)", toString(t, false))
        //test preorder
        assertEquals("(8,8) (7,7) (13,13)", toStringPre(t, false))
        //******************************************
        // in between size test
        //******************************************
        // isEmpty must return false
        assertEquals(false, t.size == 0)
        // size must be 3
        assertEquals(3, t.size.toLong())
        //******************************************
        //  remove everything except root
        //******************************************
        t.remove(7)
        t.remove(13)
        //test inorder
        assertEquals("(8,8)", toString(t, false))
        //test preorder
        assertEquals("(8,8)", toStringPre(t, false))
        //******************************************
        //  remove last node
        //******************************************
        t.remove(8)
        // root should be null
        Assert.assertEquals(null, t.root)
        // isEmpty() should return true
        assertEquals(true, t.size == 0)
        // the size should be 0
        assertEquals(0, t.size.toLong())
        //test inorder
        assertEquals("", toString(t, false))
        //test preorder
        assertEquals("", toStringPre(t, false))
    }

    /**
     * tests: - remove with checking the height. The height is checked while doing the inorder an
     * preorder tests. - size()
     */
    @Test
    fun deleteHeightTest() {
        val t = getImplementation<Int, String>()
        t.put(6, "6")
        t.put(1, "1")
        t.put(4, "4")
        t.put(11, "11")
        t.put(8, "8")
        t.put(7, "7")
        t.put(13, "13")
        t.put(10, "10")
        t.put(9, "9")
        //*************************************************************
        //remove an inner node with having both left and right subtree
        //*************************************************************
        assertEquals("11", t.remove(11))    //test if remove returns the deleted Element
        //after deleting 11 ...
        // ... right child of root should be 10
        assertEquals("(10,10)", toString(t.root!!.rightChild))
        // ... left child of 10 should be 8
        assertEquals("(8,8)", toString(t.root!!.rightChild!!.leftChild))
        // ... right child of 8 should be 9
        assertEquals("(9,9)", toString(t.root!!.rightChild!!.leftChild!!.rightChild))
        // parent update check: parent of 9 should be 8
        assertEquals("(8,8)", toString(t.root!!.rightChild!!.leftChild!!.rightChild!!.parent))
        //test inorder + height check
        assertEquals("(1,1,1) (4,4,0) (6,6,3) (7,7,0) (8,8,1) (9,9,0) (10,10,2) (13,13,0)", toString(t, true))
        //test preorder + height check
        assertEquals("(6,6,3) (1,1,1) (4,4,0) (10,10,2) (8,8,1) (7,7,0) (9,9,0) (13,13,0)", toStringPre(t, true))
        //****************
        //remove leaf node
        //****************
        t.remove(9)
        // left child of 8 should be 7
        assertEquals("(7,7)", toString(t.root!!.rightChild!!.leftChild!!.leftChild))
        //  right child should be null
        Assert.assertEquals(null, t.root!!.rightChild!!.leftChild!!.rightChild)
        //test inorder + height check
        assertEquals("(1,1,1) (4,4,0) (6,6,3) (7,7,0) (8,8,1) (10,10,2) (13,13,0)", toString(t, true))
        //test preorder + height check
        assertEquals("(6,6,3) (1,1,1) (4,4,0) (10,10,2) (8,8,1) (7,7,0) (13,13,0)", toStringPre(t, true))
        //************************************************************************************
        //remove an inner node having both subtrees, but largest node is child of deleted node
        //************************************************************************************
        t.remove(10)
        // right child of root should be 8
        assertEquals("(8,8)", toString(t.root!!.rightChild))
        //left child of 8 should be 7
        assertEquals("(7,7)", toString(t.root!!.rightChild!!.leftChild))
        //right child of 8 should be 13
        assertEquals("(13,13)", toString(t.root!!.rightChild!!.rightChild))
        // parent check: parent of 7 should be 8
        assertEquals("(8,8)", toString(t.root!!.rightChild!!.leftChild!!.parent))
        //test inorder + height check
        assertEquals("(1,1,1) (4,4,0) (6,6,2) (7,7,0) (8,8,1) (13,13,0)", toString(t, true))
        //test preorder + height check
        assertEquals("(6,6,2) (1,1,1) (4,4,0) (8,8,1) (7,7,0) (13,13,0)", toStringPre(t, true))
        //******************************************
        // remove an inner node with no left subtree
        //******************************************
        t.remove(1)
        // left child of root should be 4
        assertEquals("(4,4)", toString(t.root!!.leftChild))
        // parent of 4 should be root (6)
        assertEquals("(6,6)", toString(t.root!!.leftChild!!.parent))
        assertEquals("(6,6)", toString(t.root))
        //test inorder + height check
        assertEquals("(4,4,0) (6,6,2) (7,7,0) (8,8,1) (13,13,0)", toString(t, true))
        //test preorder + height check
        assertEquals("(6,6,2) (4,4,0) (8,8,1) (7,7,0) (13,13,0)", toStringPre(t, true))
        //*********
        // remove 4
        //*********
        t.remove(4)
        // left child of root should be null
        Assert.assertEquals(null, t.root!!.leftChild)
        //test inorder + height check
        assertEquals("(6,6,2) (7,7,0) (8,8,1) (13,13,0)", toString(t, true))
        //test preorder + height check
        assertEquals("(6,6,2) (8,8,1) (7,7,0) (13,13,0)", toStringPre(t, true))
        //******************************************
        // remove root with no left subtree
        //******************************************
        t.remove(6)
        // root should be 8
        assertEquals("(8,8)", toString(t.root))
        //test inorder + height check
        assertEquals("(7,7,0) (8,8,1) (13,13,0)", toString(t, true))
        //test preorder + height check
        assertEquals("(8,8,1) (7,7,0) (13,13,0)", toStringPre(t, true))
        //******************************************
        // in between size test
        //******************************************
        // isEmpty must return false
        assertEquals(false, t.size == 0)
        // size must be 3
        assertEquals(3, t.size.toLong())
        //******************************************
        //  remove everything except root
        //******************************************
        t.remove(7)
        t.remove(13)
        //test inorder + height check
        assertEquals("(8,8,0)", toString(t, true))
        //test preorder + height check
        assertEquals("(8,8,0)", toStringPre(t, true))
        //******************************************
        //  remove last node
        //******************************************
        t.remove(8)
        // root should be null
        Assert.assertEquals(null, t.root)
        // isEmpty() should return true
        assertEquals(true, t.size == 0)
        // the size should be 0
        assertEquals(0, t.size.toLong())
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
        t.put(7, "7")
        t.put(4, "4")
        t.put(3, "3")
        t.put(11, "11")
        t.put(13, "13")
        t.put(8, "8")
        t.put(5, "5")
        t.put(1, "1")
        t.put(2, "2")
        t.put(6, "6")
        t.put(0, "0")
        assertEquals("[3, 4, 5, 6, 7]", t.valueRange(3, 7).toString())
        // find the node (11), which is no leaf
        assertEquals("[11]", t.valueRange(9, 12).toString())
        // find keys larger than the search area
        assertEquals("[]", t.valueRange(-5, -1).toString())
        // find keys smaller than the search area
        assertEquals("[]", t.valueRange(14, 17).toString())
        // test with string keys
        val t2 = getImplementation<String, String>()
        t2.put("a", "a")
        t2.put("aa", "aa")
        t2.put("ab", "ab")
        t2.put("b", "b")
        t2.put("ca", "ca")
        t2.put("cac", "cac")
        t2.put("x", "x")
        t2.put("y", "y")
        t2.put("aaba", "aaba")
        assertEquals("[aa, aaba, ab, b]", t2.valueRange("aa", "c").toString())
    }
}
