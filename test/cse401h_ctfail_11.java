//
// Test compile time errors for unreachable statements
//
class cse401h_ctfail_11 {

  public static void main (String [] args) {
    System.out.println(new TestUnreachableStatements().run());
  }

}

class TestUnreachableStatements {

  public int run() {
    int x;

    System.out.println(20000011);

    x = 1;
    return x;

    x = 2;
    return x;
  }

}
