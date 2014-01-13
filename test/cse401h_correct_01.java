//
// Test if we can print an int after doing arithmetic
//
class TestPrintIntArithmetic {

  public static void main (String [] args) {
    System.out.println(TestWorker.run());
  }

}

class TestWorker {

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
