//
// Exercise some tricky issues involving double precision numbers.
//
class ExerciseDouble {

  public static void main (String [] args) {
    System.out.println(new Worker().OneFloat());
    // System.out.println(new Worker().AddFloatInt(2.718281828459045, 1));
  }

}

class Worker {

  int x;

  public double AddFloatInt(double d, int i) {
    double back;
    if (i == 1) {
      back = d + 1.0;
    } else {
      back = d + 2.0;
    }
    return back;
  }

  public double OneFloat() {
    double [] A;
    int i;
    double d;
    double aNaN;
    double aInf;

    if (3.0 < 4.0) {
      System.out.println(0);
    } else {
      System.out.println(9999);
    }

    if (3.0 <= 3.0) {
      System.out.println(0);
    } else {
      System.out.println(9999);
    }

    if (3.0 == 3.0) {
      System.out.println(0);
    } else {
      System.out.println(9999);
    }

    if (3.0 != 4.0) {
      System.out.println(0);
    } else {
      System.out.println(9999);
    }

    if (4.0 != 3.0) {
      System.out.println(0);
    } else {
      System.out.println(9999);
    }

    if (4.0 >= 4.0) {
      System.out.println(0);
    } else {
      System.out.println(9999);
    }
    if (4.0 >= 3.0) {
      System.out.println(0);
    } else {
      System.out.println(9999);
    }

    aNaN = 0.0 / 0.0;
    if (aNaN != aNaN) {
      System.out.println(0);
    } else {
      System.out.println(8888);
    }

    if (3.0 < aNaN) {
      System.out.println(8887);
    } else {
      System.out.println(0);
    }
    if (3.0 <= aNaN) {
      System.out.println(8886);
    } else {
      System.out.println(0);
    }
    if (3.0 == aNaN) {
      System.out.println(8885);
    } else {
      System.out.println(0);
    }
    if (3.0 != aNaN) {
      System.out.println(0);  // NaNs always compare unequal
    } else {
      System.out.println(8884);
    }
    if (3.0 >= aNaN) {
      System.out.println(8883);
    } else {
      System.out.println(0);
    }
    if (3.0 > aNaN) {
      System.out.println(8882);
    } else {
      System.out.println(0);
    }

    if (aNaN < 3.0) {
      System.out.println(7777);
    } else {
      System.out.println(0);
    }
    if (aNaN <= 3.0) {
      System.out.println(7776);
    } else {
      System.out.println(0);
    }
    if (aNaN == 3.0) {
      System.out.println(7775);
    } else {
      System.out.println(0);
    }
    if (aNaN != 3.0) {
      System.out.println(0);  // NaNs always compare unequal
    } else {
      System.out.println(7774);
    }
    if (aNaN >= 3.0) {
      System.out.println(7773);
    } else {
      System.out.println(0);
    }
    if (aNaN > 3.0) {
      System.out.println(7772);
    } else {
      System.out.println(0);
    }

    A = new double[20];
    i = 0;
    d = 0.0;
    while (i < 20) {
      A[i] = d;
      d = d + 1.0;
      i = i + 1;
    }

    return 500.00e-03 * 2.0e+000;
  }

}
