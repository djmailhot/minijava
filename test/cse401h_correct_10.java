//
// Test doubles
//
class cse401h_correct_10 {

  public static void main(String[] args) {
    System.out.println(new TestDoubles().run());
  }

}

class TestDoubles {

  public int run() {
    System.out.println(100000010);

    System.out.println(1.0);
    System.out.println(1.0d);
    System.out.println(1.545D);
    System.out.println(5454.232e+10d);
    System.out.println(543.453e4D);
    System.out.println(87.);
    System.out.println(.10);
    System.out.println(4.9e-2);
    System.out.println(5d);
    System.out.println(3.d);
    System.out.println(5e00);
    System.out.println(5e-2D);

    System.out.println(1.0 + 1.0);
    System.out.println(1.0 - 1.0);
    System.out.println(2.0 * 2.0);
    System.out.println(4.0 / 2.0);

    System.out.println(5.0 / 2.0);
    System.out.println(22.0 / 7.0);
    System.out.println(3.125 * 4d);

    System.out.println(-4.0 * 1.0);
    System.out.println(-4.0 * -1.0);
    System.out.println(-4.0 / -1.0);

    if (0.0 == 0.0)
      System.out.println(1);
    if (0.0 == 1.0)
      System.out.println(0);

    if (0.0 != 0.0)
      System.out.println(0);
    if (0.0 != 1.0)
      System.out.println(2);

    if (0.0 < 1.0)
      System.out.println(3);
    if (1.0 < 1.0)
      System.out.println(0);
    if (1.0 < 0.0)
      System.out.println(0);

    if (1.0 > 0.0)
      System.out.println(4);
    if (1.0 > 1.0)
      System.out.println(0);
    if (0.0 > 1.0)
      System.out.println(0);

    if (0.0 <= 1.0)
      System.out.println(5);
    if (1.0 <= 1.0)
      System.out.println(6);
    if (1.0 <= 0.0)
      System.out.println(0);

    if (1.0 >= 0.0)
      System.out.println(7);
    if (1.0 >= 1.0)
      System.out.println(8);
    if (0.0 >= 1.0)
      System.out.println(0);

    return 0;
  }

}
