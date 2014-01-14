//
// Test compiler errors for the MainClass
//
class cse401h_ctfail_04 {

  public static void main (String [] args) {
    System.out.println(new TestIncompatibleReturnTypes().run());
  }

}

class TestIncompatibleReturnTypes {

  public int run() {
    int x;

    x = truth();
    return x;
  }

  public int truth() {
    return true;
  }

}
