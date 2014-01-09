class DoubleOutput {
  public static void main(String[] a) {
    System.out.println(
      new DoubleOutputWorker().doWork()
    );
  }
}

class DoubleOutputWorker {
  public double doE() {
    return 2.718281828459045;
  }
  //
  // See http://docs.oracle.com/javase/6/docs/api/java/lang/Double.html#toString(double)
  //
  public double doWork() {
    System.out.println(0.0 / 0.0);  // NaN
    System.out.println(1.0 / 0.0);  // Infinity
    System.out.println((0.0 - 1.0) / 0.0);  // -Infinity
    System.out.println(0.0 - 0.0);
    System.out.println(1.0);
    System.out.println(1.5);
    System.out.println(    1.0 * 2.718281828459045);
    System.out.println(   10.0 * 2.718281828459045);
    System.out.println(  100.0 * 2.718281828459045);
    System.out.println( 1000.0 * 2.718281828459045);
    System.out.println(10000.0 * 2.718281828459045);

    System.out.println(0.5);
    System.out.println(0.05);
    System.out.println(0.005);
    System.out.println(0.0005);

    System.out.println(2.0);
    System.out.println(20.0);
    System.out.println(200.0);
    System.out.println(2000.0);
    System.out.println(20000.0);
    System.out.println(200000.0);
    System.out.println(2000000.0);
    System.out.println(20000000.0);
    System.out.println(200000000.0);

    return 9999.0;
  }

}
