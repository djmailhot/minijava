//
// Compute the arcsin.
//
class Asin {
  public static void main(String[] a) {
    System.out.println(new AsinWorker().driver());
  }
}

class AsinWorker {

  public int driver() {
    System.out.println(this.asin(1.0));
    System.out.println(this.asin(0.5));
    System.out.println(this.asin(2.0));

    System.out.println(this.asin(0.0 - 1.0));
    System.out.println(this.asin(0.0 - 0.5));
    System.out.println(this.asin(0.0 - 2.0));

    System.out.println(2.0 * this.asin(1.0));

    return 9999;
  }

  public double Pi() {
    return 3.14159265358979323846264;
  }

  //
  // See http://golang.org/src/pkg/math/asin.go
  //
  public double asin(double x) {
    boolean sign;
    double back;
    double temp;

    if (x == 0.0) {
      temp = x; // special case 0.0
    } else {
      if (x < 0.0) {
        x = 0.0 - x;
        sign = true;
      } else {
        sign = false;
      }
      if (x > 1.0) {
        temp = this.NaN(); // special case
      } else {
        temp = this.Sqrt(1.0 - x*x);
        if (x > 0.7) {
          temp = this.Pi()/2.0 - this.satan(temp/x);
        } else {
          temp = this.satan(x / temp);
        }
        if (sign) {
          temp = 0.0 - temp;
        } else {
          temp = temp;
        }
      }
    }
    return temp;
  }

  public double NaN() {
    return 0.0 / 0.0;
  }

  public double Abs(double v) {
    double abs;
    if (v < 0.0) {
      abs = 0.0 - v; // cheesy in absence of unary negate
    } else {
      abs = v;
    }
    return abs;
  }

  //
  // see http://stackoverflow.com/questions/8971659/explanation-of-newtons-method-example-on-java
  //
  // A VERY Poor implementation that uses Newton's method.
  //
  public double Sqrt(double c) {
    double epsilon;    // relative error tolerance
    double t;          // estimate of the square root of c

    epsilon = 1e-15;
    t = c;

    //
    // Repeatedly apply Newton update step until the desired precision is achieved.
    //
    while (this.Abs(t - c/t) > epsilon * t) {
      t = (c/t + t) / 2.0;
    }
    return t;
  }

  //
  // See http://golang.org/src/pkg/math/atan.go
  //
  // satan reduces its argument (known to be positive)
  // to the range [0, 0.66] and calls xatan.
  //
  public double satan(double x) {
    double Morebits;
    double Tan3pio8;
    double back;

    Morebits = 6.123233995736765886130e-17; // pi/2 = PIO2 + Morebits
    Tan3pio8 = 2.41421356237309504880;      // tan(3*pi/8)

    if (x <= 0.66) {
      back = this.xatan(x);
    } else {
      if (x > Tan3pio8) {
        back = this.Pi()/2.0 - this.xatan(1.0/x) + Morebits;
      } else {
        back = this.Pi()/4.0 + this.xatan((x-1.0)/(x+1.0)) + 0.5*Morebits;
      }
    }
    return back;
  }

  //
  // See http://golang.org/src/pkg/math/atan.go
  //
  // xatan evaluates a series valid in the range [0, 0.66].
  //
  public double xatan(double x) {
    double z;
    double P0;
    double P1;
    double P2;
    double P3;
    double P4;
    double Q0;
    double Q1;
    double Q2;
    double Q3;
    double Q4;
    
    P0 = 0.0 - 8.750608600031904122785e-01;
    P1 = 0.0 - 1.615753718733365076637e+01;
    P2 = 0.0 - 7.500855792314704667340e+01;
    P3 = 0.0 - 1.228866684490136173410e+02;
    P4 = 0.0 - 6.485021904942025371773e+01;

    Q0 = 0.0 + 2.485846490142306297962e+01;
    Q1 = 0.0 + 1.650270098316988542046e+02;
    Q2 = 0.0 + 4.328810604912902668951e+02;
    Q3 = 0.0 + 4.853903996359136964868e+02;
    Q4 = 0.0 + 1.945506571482613964425e+02;

    z = x * x;
    z = z * ((((P0*z+P1)*z+P2)*z+P3)*z + P4) / (((((z+Q0)*z+Q1)*z+Q2)*z+Q3)*z + Q4);
    z = x*z + x;
    return z;
  }

}
