//
// Test runtime errors for negative array size
//
class cse401h_rtfail_02 {

  public static void main (String [] args) {
    System.out.println(new TestNegativeArraySize().run());
  }

}

class TestNegativeArraySize {

  public int run() {
    int x;
    int[] a;

    System.out.println(300000002);

    x = 1 - 2;

    a = new int[x];
    a[0] = 0;

    return a[1];
  }

}
