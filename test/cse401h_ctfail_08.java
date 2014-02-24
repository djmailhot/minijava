//
// Test for cycles in class hierarchy
//
class cse401h_ctfail_08 {

  public static void main (String [] args) {
    System.out.println(new TestClassHierarchyCycle().run());
  }

}

class TestClassHierarchyCycle {

  public int run() {
    System.out.println(20000008);
    return 1;
  }

}

class A extends C {
  public int m0() {
    return 0;
  }
}

class B extends A {
  public int m1() {
    return 1;
  }
}

class C extends B {
  public int m2() {
    return 2;
  }
}
