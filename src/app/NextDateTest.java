package app;

import junit.framework.*;
public class NextDateTest extends TestCase {
  public void testCase1() {
   NextDate x1 = new NextDate();
   java.lang.Integer x2 = new java.lang.Integer(1209);
   java.lang.Integer x3 = new java.lang.Integer(705);
   java.lang.Integer x4 = new java.lang.Integer(2273);
   x1.run(x2,x3,x4);
  }

  public void testCase2() {
   NextDate x1 = new NextDate();
   java.lang.Integer x2 = new java.lang.Integer(12);
   java.lang.Integer x3 = new java.lang.Integer(2321);
   java.lang.Integer x4 = new java.lang.Integer(1808);
   x1.run(x2,x3,x4);
  }

  public void testCase3() {
   NextDate x1 = new NextDate();
   java.lang.Integer x2 = new java.lang.Integer(10);
   java.lang.Integer x3 = new java.lang.Integer(10);
   java.lang.Integer x4 = new java.lang.Integer(1852);
   x1.run(x2,x3,x4);
  }

  public void testCase4() {
   NextDate x1 = new NextDate();
   java.lang.Integer x2 = new java.lang.Integer(3);
   java.lang.Integer x3 = new java.lang.Integer(160);
   java.lang.Integer x4 = new java.lang.Integer(1890);
   x1.run(x2,x3,x4);
  }

  public void testCase5() {
   NextDate x1 = new NextDate();
   java.lang.Integer x2 = new java.lang.Integer(4);
   java.lang.Integer x3 = new java.lang.Integer(1131);
   java.lang.Integer x4 = new java.lang.Integer(1926);
   x1.run(x2,x3,x4);
  }

  public void testCase6() {
   NextDate x1 = new NextDate();
   java.lang.Integer x2 = new java.lang.Integer(2);
   java.lang.Integer x3 = new java.lang.Integer(68);
   java.lang.Integer x4 = new java.lang.Integer(1989);
   x1.run(x2,x3,x4);
  }

  public static void main (String[] args) {
    junit.textui.TestRunner.run(NextDateTest.class);
  }
}