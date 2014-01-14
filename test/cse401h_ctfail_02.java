//
// Test compiler errors for the MainClass
//
class cse401h_ctfail_02 {

  public static void main (String [] args) {
    System.out.println(new TestArrayIndexOutOfBounds().run());
  }

}

class TestArrayIndexOutOfBounds {

  public int run() {
    int[] a;

    a = new int[1];
    a[0] = 0;
    a[1] = 1;

    return a[1];
  }

}
