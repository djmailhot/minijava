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
    System.out.println(30000002);

    int[] a;

    a = new int[-1];
    a[0] = 0;

    return a[1];
  }

}
