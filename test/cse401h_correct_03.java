//
// Test while statements
//
class cse401h_correct_03 {

  public static void main(String[] args) {
    System.out.println(new TestWhile().run());
  }

}

class TestWhile {

  public int run() {
    int i;

    i = 0;
    while (i < 5)
      System.out.println(5);

    i = 1;
    while (i <= 10) {
      System.out.println(i);
      i = i + 1;
    }

    return 1;
  }

}
