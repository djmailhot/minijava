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
    double x;

    System.out.println(100000007);

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

    return 0;
  }

}
