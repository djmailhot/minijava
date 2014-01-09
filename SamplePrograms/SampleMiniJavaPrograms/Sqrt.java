//
// Use Newton's method to compute an approximation to sqrt.
//
// THERE ARE MUCH FASTER AND MORE PRECISE ALGORITHMS THAN THIS.
// CONSULT ANY GOOD NUMERICAL ANALYSIS TEXTBOOK.
//
// Sqrt is studied extensively.
// Sqrt is efficiently implemented in hardware on some platforms.
//
class Sqrt {
  public static void main(String[] a) {
    System.out.println(
      new SqrtWorker().Sqrt(2.0)
    );
  }
}

class SqrtWorker {
  //
  // see http://stackoverflow.com/questions/8971659/explanation-of-newtons-method-example-on-java
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

  public double Abs(double v) {
    double abs;
    if (v < 0.0) {
      abs = 0.0 - v; // cheesy in absence of unary negate
    } else {
      abs = v;
    }
    return abs;
  }

}
