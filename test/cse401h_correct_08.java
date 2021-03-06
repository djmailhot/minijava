//
// Test comparison operators on doubles
//
class cse401h_correct_08 {

  public static void main(String[] args) {
    System.out.println(new TestCompareDoubles().run());
  }

}

class TestCompareDoubles {

  public int run() {
    System.out.println(100000008);

    System.out.println(this.compare(0.0 == 0.0));
    System.out.println(this.compare(0.0 == 1.0));

    System.out.println(this.compare(0.0 != 0.0));
    System.out.println(this.compare(0.0 != 1.0));

    System.out.println(this.compare(0.0 < 1.0));
    System.out.println(this.compare(1.0 < 1.0));
    System.out.println(this.compare(1.0 < 0.0));

    System.out.println(this.compare(1.0 > 0.0));
    System.out.println(this.compare(1.0 > 1.0));
    System.out.println(this.compare(0.0 > 1.0));

    System.out.println(this.compare(0.0 <= 1.0));
    System.out.println(this.compare(1.0 <= 1.0));
    System.out.println(this.compare(1.0 <= 0.0));

    System.out.println(this.compare(1.0 >= 0.0));
    System.out.println(this.compare(1.0 >= 1.0));
    System.out.println(this.compare(0.0 >= 1.0));

    return 0;
  }

  public int compare(boolean result) {
    int i;
    if (result) {
      i = 1;
    } else {
      i = 0;
    }
    return i;
  }

}
