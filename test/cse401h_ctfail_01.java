//
// Test compile time errors for incompatible assignment types
//
class cse401h_ctfail_01 {

  public static void main (String [] args) {
    System.out.println(new TestIncompatibleAssignmentTypes().run());
  }

}

class TestIncompatibleAssignmentTypes {

  public int run() {
    int x;
    int y;
    int z;

    System.out.println(200000001);

    x = 1;
    y = 2;
    z = x < y;

    return z;
  }

}
