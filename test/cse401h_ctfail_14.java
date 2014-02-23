//
// Test illegal array lookup
//
class cse401h_ctfail_14 {

  public static void main (String [] args) {
    System.out.println(new TestIllegalArrayLookup().run());
  }

}

class TestIllegalArrayLookup {

  public int run() {
    int x;
    int y;

    System.out.println(20000014);

    y = x[0];

    return y;
  }

}
