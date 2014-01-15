//
// Test compile time errors for incompatible method arguments
//
class cse401h_ctfail_07 {

  public static void main (String [] args) {
    System.out.println(new TestIncompatibleMethodArguments().run());
  }

}

class TestIncompatibleMethodArguments {

  public int run() {
    System.out.println(200000007);

    int x;
    int y;
    int z;

    x = 0;
    y = 1;
    z = method(x, y);

    return z;
  }

  public int method(int x, boolean a) {
    return x;
  }

}
