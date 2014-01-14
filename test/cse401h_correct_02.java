//
// Test integer comparison and if/else branching
//
class cse401h_correct_02 {

  public static void main(String[] args) {
    System.out.println(new TestIfElse().run());
  }

}

class TestIfElse {

  public int run() {
    // Test comparison operators.
    // Printing 0 indicates a failed test.
    if (0 == 0)
      System.out.println(1);
    if (0 == 1)
      System.out.println(0);

    if (0 != 0)
      System.out.println(0);
    if (0 != 1)
      System.out.println(2);

    if (0 < 1)
      System.out.println(3);
    if (1 < 1)
      System.out.println(0);
    if (1 < 0)
      System.out.println(0);

    if (1 > 0)
      System.out.println(4);
    if (1 > 1)
      System.out.println(0);
    if (0 > 1)
      System.out.println(0);

    if (0 <= 1)
      System.out.println(5);
    if (1 <= 1)
      System.out.println(6);
    if (1 <= 0)
      System.out.println(0);

    if (1 >= 0)
      System.out.println(7);
    if (1 >= 1)
      System.out.println(8);
    if (0 >= 1)
      System.out.println(0);

    // Test else branching
    if (0 == 0)
      System.out.println(9);
    else
      System.out.println(0);

    if (0 == 1)
      System.out.println(0);
    else
      System.out.println(10);

    // Test "else if"
    if (42 > 666) {
      System.out.println(0);
    } else if (1 >= 10) {
      System.out.println(0);
    } else {
      System.out.println(11);
    }

    return 12;
  }

}
