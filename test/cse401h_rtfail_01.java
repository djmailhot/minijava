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
    System.out.println(30000001);

    int[] a;

    a = new int[1];
    a[0] = 0;
    a[1] = 1;

    return a[1];
  }

}
