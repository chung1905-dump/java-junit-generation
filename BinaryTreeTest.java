import junit.framework.*;

public class BinaryTreeTest extends TestCase {
  public void testCase1() {
    BinaryTree x818 = new BinaryTree();
    java.lang.Integer x873 = new java.lang.Integer(41);
    BinaryTreeNode x872 = new BinaryTreeNode(x873);
    x818.insert(x872);
    java.lang.Integer x874 = new java.lang.Integer(93);
    x818.search(x874);
    java.lang.Integer x876 = new java.lang.Integer(5);
    BinaryTreeNode x875 = new BinaryTreeNode(x876);
    x818.insert(x875);
    java.lang.Integer x878 = new java.lang.Integer(31);
    BinaryTreeNode x877 = new BinaryTreeNode(x878);
    x818.insert(x877);
    java.lang.Integer x869 = new java.lang.Integer(31);
    x818.search(x869);
    java.lang.Integer x871 = new java.lang.Integer(65);
    BinaryTreeNode x870 = new BinaryTreeNode(x871);
    x818.insert(x870);
    java.lang.Integer x819 = new java.lang.Integer(63);
    x818.search(x819);
  }

  public void testCase2() {
    BinaryTree x24 = new BinaryTree();
    java.lang.Integer x25 = new java.lang.Integer(86);
    x24.search(x25);
    java.lang.Integer x27 = new java.lang.Integer(70);
    BinaryTreeNode x26 = new BinaryTreeNode(x27);
    x24.insert(x26);
    java.lang.Integer x29 = new java.lang.Integer(78);
    BinaryTreeNode x28 = new BinaryTreeNode(x29);
    x24.insert(x28);
  }

  public static void main (String[] args) {
    junit.textui.TestRunner.run(BinaryTreeTest.class);
  }
}
