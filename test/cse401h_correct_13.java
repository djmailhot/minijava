//
// Test doubles comprehensively
//
class cse401h_correct_13 {

  public static void main (String [] args) {
    System.out.println(new TestDoublesMOAR().run());
  }
}

class TestDoublesMOAR {
  public int run() {

    System.out.println(100000013);

    System.out.println(this.arithmetic());

    System.out.println(this.comparisons());

    System.out.println(this.params(1.0, 1, (4.0 / 3.0), 2));

    System.out.println(this.arrays());

    System.out.println(this.representations());

    return 1;
  }

  public double arithmetic() {
    System.out.println(0.2 + 0.8);
    System.out.println(4.2 - 2.2);
    System.out.println(0.5 * 6.0);
    System.out.println(2.0 / 0.5);

    System.out.println((0.0 - 4.0) * 1.25);
    System.out.println((0.0 - 4.0) / (0.0 - 0.666666666));

    return 1.0 / 3.0;
  }

  public double comparisons() {
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

    return 2.0 / 3.0;
  }

  public double params(double w, int x, double y, int z) {
    x = x / z;
    System.out.println(x);
    w = w / y;
    System.out.println(w);
    return y;
  }

  public double arrays() {
    int i;
    double v;
    double[] a;
    double[] b;

    i = 1;
    v = 0.0;
    a = new double[10];
    b = new double[99];

    while (i < a.length) {
      a[i] = v;
      System.out.println(a[i]);
      i = i + 1;
      v = v + 0.1;
    }

    while (i < b.length) {
      b[i] = v;
      System.out.println(b[i]);
      i = i + 1;
      v = v * 1.1;
    }

    return 5.0 / 3.0;
  }

  public double representations() {
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

    return 0.0;
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
