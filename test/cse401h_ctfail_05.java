//
// Test compile time errors for duplicate variable names
//
class cse401h_ctfail_05 {

  public static void main (String [] args) {
    System.out.println(new TestDuplicateVariableNames().run());
  }

}

class TestDuplicateVariableNames {

  public int run() {
    System.out.println(200000005);

    int x;
    x = 0;

    int x;
    x = 1;
    return x;
  }

}
