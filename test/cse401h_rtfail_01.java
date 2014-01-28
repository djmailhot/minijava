//
// Test runtime errors for array indexing out of bounds
//
class cse401h_rtfail_01 {

  public static void main (String [] args) {
    System.out.println(new TestArrayIndexOutOfBounds().run());
  }

}

class TestArrayIndexOutOfBounds {

  public int run() {
    int[] a;

    System.out.println(300000001);

    a = new int[1];
    a[0] = 0;
    a[1] = 1;

    return a[1];
  }

}
