//
// Test compile time errors for missing type identifiers
//
class cse401h_ctfail_00 {

  public static void main (String [] args) {
    System.out.println(new TestMissingType().run());
  }

}

class TestMissingType {

  public int run() {
    System.out.println(200000000);

    x = 0;
    System.out.println(x);
    return x;
  }

}
