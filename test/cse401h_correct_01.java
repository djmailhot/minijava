//
// Test if we can print an int after doing arithmetic
//
class cse401h_correct_01 {

  public static void main (String [] args) {
    System.out.println(TestPrintIntArithmetic.run());
  }

}

class TestPrintIntArithmetic {

  public int run() {
    int x;

    x = 4 - 3
    System.out.println(x);
    x = 4 / 2
    System.out.println(x);
    x = 1 + 2
    System.out.println(x);
    x = 2 * 2
    System.out.println(x);
    return 1;
  }

}
