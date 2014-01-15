//
// Test compile time errors incompatible return types
//
class cse401h_ctfail_03 {

  public static void main (String [] args) {
    System.out.println(new TestIncompatibleReturnTypes().run());
  }

}

class TestIncompatibleReturnTypes {

  public int run() {
    System.out.println(200000003);

    int x;

    x = truth();
    return x;
  }

  public int truth() {
    return true;
  }

}
