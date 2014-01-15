//
// Test arithmetic operators on doubles
//
class cse401h_correct_07 {

  public static void main(String[] args) {
    System.out.println(new TestDoubles().run());
  }

}

class TestDoubles {

  public int run() {
    System.out.println(100000007);

    double x;

    x = 5.0 + 3.0;
    System.out.println(x);
    x = 5.0 - 3.0;
    System.out.println(x);
    x = 4.0 / 2.0;
    System.out.println(x);
    x = 5.0 / 2.0;
    System.out.println(x);
    x = 5.0 * 2.0;
    System.out.println(x);
    x = 5.5 * 2.0;
    System.out.println(x);
    x = 5.0 % 3.0;
    System.out.println(x);
    x = 5.2 % 3.0;
    System.out.println(x);
    x = 5.0 % 3.5;
    System.out.println(x);

    return 0;
  }

}
