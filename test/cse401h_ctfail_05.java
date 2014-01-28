//
// Test compile time errors for variable redeclaration
//
class cse401h_ctfail_05 {

  public static void main (String [] args) {
    System.out.println(new TestDuplicateVariableNames().run());
  }

}

class TestDuplicateVariableNames {

  public int run() {
    int x;

    System.out.println(200000005);

    x = 0;

    int x;
    x = 1;
    return x;
  }

}
