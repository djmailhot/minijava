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
    System.out.println(100000009);

    // Test return value
    System.out.println(returnFive());

    // Test argument passing
    System.out.println(identity(42));

    // Test argument ordering
    System.out.println(divide(4, 2));
    passedOneThroughSix(1, 2, 3, 4, 5, 6);

    // Test nested function calls
    System.out.println(nest1(42));

    // Test recursion
    System.out.println(recursiveCount(42));

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

  public boolean passedOneThroughSix(int a, int b, int c, int d, int e, int f) {
    boolean correct = true;

    correct = correct && (a == 1);
    correct = correct && (b == 2);
    correct = correct && (c == 3);
    correct = correct && (d == 4);
    correct = correct && (e == 5);
    correct = correct && (f == 6);

    return correct;
  }

  public int nest1(int x) {
    System.out.println(1);
    System.out.println(nest2(x * 2));
    return x;
  }

  public int nest2(int x) {
    System.out.println(2);
    System.out.println(nest3(x * 2));
    return x;
  }

  public int nest3(int x) {
    System.out.println(3);
    System.out.println(nest4(x * 2));
    return x;
  }

  public int nest4(int x) {
    System.out.println(4);
    System.out.println(nest5(x * 2));
    return x;
  }

  public int nest5(int x) {
    System.out.println(5);
    System.out.println(x * 2);
    return x;
  }

  public int recursiveCount(int n) {
    if (n == 0)
      System.out.println(n);
    else {
      recursiveCount(n - 1);
      System.out.println(n);
    }
    return 0;
  }

}
