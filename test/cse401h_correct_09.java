//
// Test function calls
//
class cse401h_correct_09 {

  public static void main(String[] args) {
    System.out.println(new TestFunctions().run());
  }

}

class TestFunctions {

  public int run() {
    int ret;

    System.out.println(100000009);

    // Test return value
    System.out.println(this.returnFive());

    // Test argument passing
    System.out.println(this.identity(42));

    // Test argument ordering
    System.out.println(this.divide(4, 2));
    ret = this.passedOneThroughSix(1, 2, 3, 4, 5, 6);

    // Test nested function calls
    System.out.println(this.nest1(42));

    // Test recursion
    System.out.println(this.recursiveCount(42));

    return 0;
  }

  public int returnFive() {
    return 5;
  }

  public int identity(int x) {
    return x;
  }

  public int divide(int x, int y) {
    return x / y;
  }

  public int passedOneThroughSix(int a, int b, int c, int d, int e, int f) {
    boolean correct;
    int ret;

    correct = true;

    correct = correct && (a == 1);
    correct = correct && (b == 2);
    correct = correct && (c == 3);
    correct = correct && (d == 4);
    correct = correct && (e == 5);
    correct = correct && (f == 6);

    if (correct)
      ret = 1;
    else
      ret = 0;

    return ret;
  }

  public int nest1(int x) {
    System.out.println(1);
    System.out.println(this.nest2(x * 2));
    return x;
  }

  public int nest2(int x) {
    System.out.println(2);
    System.out.println(this.nest3(x * 2));
    return x;
  }

  public int nest3(int x) {
    System.out.println(3);
    System.out.println(this.nest4(x * 2));
    return x;
  }

  public int nest4(int x) {
    System.out.println(4);
    System.out.println(this.nest5(x * 2));
    return x;
  }

  public int nest5(int x) {
    System.out.println(5);
    System.out.println(x * 2);
    return x;
  }

  public int recursiveCount(int n) {
    int ret;
    if (n == 0)
      System.out.println(n);
    else {
      ret = this.recursiveCount(n - 1);
      System.out.println(n);
    }
    return 0;
  }

}
