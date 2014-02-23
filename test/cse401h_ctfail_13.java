//
// Test incorrect number of parameters passed to method
//
class cse401h_ctfail_13 {

  public static void main (String [] args) {
    System.out.println(new TestBadMethodParameters().run());
  }

}

class TestBadMethodParameters {

  public int run() {
    int x;

    System.out.println(20000013);

    x = this.testMethod(1, 2);

    return x;
  }

  public int testMethod(int x, int y, boolean b) {
    return x;
  }

}
