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
    int x;

    System.out.println(100000011);


    // Construction
    a = new int[0];
    b = new int[10];
    c = new int[100];


    // Assignment
    b[1] = 1;
    c[11] = 2;
    c[99] = 3;
    b[4] = 4;
    b[5] = 5;
    

    // Access
    System.out.println(this.checkValue(b[0], 0));
    System.out.println(this.checkValue(b[1], 1));
    System.out.println(this.checkValue(c[11], 2));
    System.out.println(this.checkValue(c[99], 3));


    // Object semantics
    a = b;
    System.out.println(this.checkValue(a[4], b[4]));
    System.out.println(this.checkValue(a[5], b[5]));

    a[0] = 6;
    System.out.println(this.checkValue(a[0], 6)); // change a, check a
    a[1] = 7;
    System.out.println(this.checkValue(b[1], 7)); // change a, check b
    b[2] = 8;
    System.out.println(this.checkValue(b[2], 8)); // change b, check b
    b[3] = 9;
    System.out.println(this.checkValue(a[3], 9)); // change b, check a


    // Length
    x = b.length;
    System.out.println(x);


    return 11;
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
