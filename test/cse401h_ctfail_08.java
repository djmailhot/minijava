//
// Test compile time errors for uninitialized variables
//
class cse401h_ctfail_08 {

  public static void main (String [] args) {
    System.out.println(new TestUninitializedVariables().run());
  }

}

class TestUninitializedVariables {

  public int run() {
    System.out.println(200000008);
    int x;

    return x;
  }

}
