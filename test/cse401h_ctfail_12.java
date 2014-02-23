//
// Test invalid print arguments
//
class cse401h_ctfail_12 {

  public static void main (String [] args) {
    System.out.println(new TestInvalidPrintArgument().run());
  }

}

class TestInvalidPrintArgument {

  public int run() {
    int x;
    boolean b;

    System.out.println(20000012);

    x = 1;
    System.out.println(x);

    b = true;
    System.out.println(b);

    return x;
  }

}
