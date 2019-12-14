package app;

import junit.framework.*;
public class CheckTriangleTest extends TestCase {
  public void testCase1() {
   CheckTriangle x1 = new CheckTriangle();
   java.lang.Double x2 = new java.lang.Double(41.51911398283111);
   java.lang.Double x3 = new java.lang.Double(21.833065360987874);
   java.lang.Double x4 = new java.lang.Double(29.27678066036965);
   x1.check(x2,x3,x4);
  }

  public void testCase2() {
   CheckTriangle x1 = new CheckTriangle();
   java.lang.Double x2 = new java.lang.Double(88.49811841092658);
   java.lang.Double x3 = new java.lang.Double(38.850196737887856);
   java.lang.Double x4 = new java.lang.Double(6.459489349910692);
   x1.check(x2,x3,x4);
  }

  public void testCase3() {
   CheckTriangle x1 = new CheckTriangle();
   java.lang.Double x2 = new java.lang.Double(25.40878356132935);
   java.lang.Double x3 = new java.lang.Double(54.73215277195964);
   java.lang.Double x4 = new java.lang.Double(20.31078826401039);
   x1.check(x2,x3,x4);
  }

  public void testCase4() {
   CheckTriangle x1 = new CheckTriangle();
   java.lang.Double x2 = new java.lang.Double(12.71204523210047);
   java.lang.Double x3 = new java.lang.Double(16.36645240048675);
   java.lang.Double x4 = new java.lang.Double(32.30530326817599);
   x1.check(x2,x3,x4);
  }

  public void testCase5() {
   CheckTriangle x1 = new CheckTriangle();
   java.lang.Integer x2 = new java.lang.Integer(98);
   java.lang.Integer x3 = new java.lang.Integer(87);
   java.lang.Integer x4 = new java.lang.Integer(19);
   java.lang.Integer x5 = new java.lang.Integer(68);
   x1.anotherCheck(x2,x3,x4,x5);
  }

  public void testCase6() {
   CheckTriangle x1 = new CheckTriangle();
   java.lang.Integer x2 = new java.lang.Integer(72);
   java.lang.Integer x3 = new java.lang.Integer(80);
   java.lang.Integer x4 = new java.lang.Integer(92);
   java.lang.Integer x5 = new java.lang.Integer(77);
   x1.anotherCheck(x2,x3,x4,x5);
  }

  public void testCase7() {
   CheckTriangle x1 = new CheckTriangle();
   java.lang.Integer x2 = new java.lang.Integer(78);
   java.lang.Integer x3 = new java.lang.Integer(4);
   java.lang.Integer x4 = new java.lang.Integer(61);
   java.lang.Integer x5 = new java.lang.Integer(4);
   x1.anotherCheck(x2,x3,x4,x5);
  }

  public void testCase8() {
   CheckTriangle x1 = new CheckTriangle();
   java.lang.Integer x2 = new java.lang.Integer(2);
   java.lang.Integer x3 = new java.lang.Integer(82);
   java.lang.Integer x4 = new java.lang.Integer(43);
   java.lang.Integer x5 = new java.lang.Integer(16);
   x1.anotherCheck(x2,x3,x4,x5);
  }

  public static void main (String[] args) {
    junit.textui.TestRunner.run(CheckTriangleTest.class);
  }
}