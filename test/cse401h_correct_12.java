//
// Test double arrays
//
class cse401h_correct_12 {

  public static void main(String[] args) {
    System.out.println(new TestDoubleArrays().run());
  }

}

class TestDoubleArrays{

  public int run() {
    double[] a;
    double[] b;
    double[] c;
    int x;

    System.out.println(100000012);


    // Construction
    a = new double[0];
    b = new double[10];
    c = new double[100];


    // Assignment
    b[1] = 1.0;
    c[11] = 2.0;
    c[99] = 3.0;
    b[4] = 4.0;
    b[5] = 5.0;
    

    // Access
    System.out.println(this.checkValue(b[0], 0.0));
    System.out.println(this.checkValue(b[1], 1.0));
    System.out.println(this.checkValue(c[11], 2.0));
    System.out.println(this.checkValue(c[99], 3.0));


    // Object semantics
    a = b;
    System.out.println(this.checkValue(a[4], b[4]));
    System.out.println(this.checkValue(a[5], b[5]));

    a[0] = 6.0;
    System.out.println(this.checkValue(a[0], 6.0)); // change a, check a
    a[1] = 7.0;
    System.out.println(this.checkValue(b[1], 7.0)); // change a, check b
    b[2] = 8.0;
    System.out.println(this.checkValue(b[2], 8.0)); // change b, check b
    b[3] = 9.0;
    System.out.println(this.checkValue(a[3], 9.0)); // change b, check a


    // Length
    x = b.length;
    System.out.println(x);


    return 11;
  }

  public double checkValue(double x, double y) {
    double ret;
    if (x == y)
      ret = x;
    else
      ret = 0.0;
    return ret;
  }

}
