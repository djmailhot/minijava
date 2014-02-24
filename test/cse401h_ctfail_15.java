//
// Test for invalid method overrides
//
class cse401h_ctfail_15 {

  public static void main (String [] args) {
    System.out.println(new TestInvalidMethodOverride().run());
  }

}

class TestInvalidMethodOverride {

  public int run() {
    System.out.println(20000015);
    return 1;
  }

}

class A {
  public int m0(int a) {
    return 0;
  }

  public int m1(int x) {
    return x;
  }
}

class B extends A {
  public int m1(int y) {
    return y;
  }
}

class C extends B {
  public int m0(boolean a) {
    return a;
  }
}
