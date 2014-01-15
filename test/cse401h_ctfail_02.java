//
// Test compile time errors for bad operand types
//
class cse401h_ctfail_02 {

  public static void main (String [] args) {
    System.out.println(new TestBadOperandTypes().run());
  }

}

class TestBadOperandTypes {

  public int run() {
    System.out.println(200000002);

    boolean a;
    int x;
    int z;

    a = true;
    x = 1;
    z = a * x;

    return z;
  }

}
