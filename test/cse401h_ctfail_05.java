//
// Test compiler errors for the MainClass
//
class cse401h_ctfail_05 {

  public static void main (String [] args) {
    System.out.println(new TestDuplicateVariableNames().run());
  }

}

class TestDuplicateVariableNames {

  public int run() {
    int x;
    x = 0;

    int x;
    x = 1;
    return x;
  }

}
