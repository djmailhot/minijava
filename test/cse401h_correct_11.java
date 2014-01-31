//
// Test arrays
//
class cse401h_correct_11 {

  public static void main(String[] args) {
    System.out.println(new TestArrays().run());
  }

}

class TestArrays{

  public int run() {
    int[] a;
    int[] b;
    int[] c;

    System.out.println(100000011);


    // Construction
    a = new int[0];
    b = new int[10];
    c = new int[100];


    // Assignment
    b[1] = 1;
    b[2] = 2;
    c[11] = 3;
    c[99] = 4;
    b[7] = 7;
    b[8] = 8;
    

    // Access
    System.out.println(this.checkValue(b[0], 0));
    System.out.println(this.checkValue(b[1], 1));
    System.out.println(this.checkValue(b[2], 2));
    System.out.println(this.checkValue(c[11], 3));
    System.out.println(this.checkValue(c[99], 4));


    // Reassignment
    b[0] = 5;
    b[1] = 6;
    System.out.println(this.checkValue(b[0], 5));
    System.out.println(this.checkValue(b[1], 6));


    // Object semantics
    a = b;
    System.out.println(this.checkValue(a[7], b[7]));
    System.out.println(this.checkValue(a[8], b[8]));

    a[0] = 9;
    System.out.println(this.checkValue(a[0], 9)); // change a, check a
    a[1] = 10;
    System.out.println(this.checkValue(b[1], 10)); // change a, check b
    b[2] = 11;
    System.out.println(this.checkValue(b[2], 11)); // change b, check b
    b[3] = 12;
    System.out.println(this.checkValue(a[3], 12)); // change b, check a


    return 13;
  }

  public int checkValue(int x, int y) {
    int ret;
    if (x == y)
      ret = x;
    else
      ret = 0;
    return ret;
  }

}
