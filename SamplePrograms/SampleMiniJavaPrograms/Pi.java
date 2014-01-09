//
// Compute digits of Pi
// See:
//   http://www.mathpropress.com/stan/bibliography/spigot.pdf
//   http://rosettacode.org/wiki/Pi#Pascal
//
// This needs MiniJava to be extended to support integer / and %
//
class Pi {
  public static void main(String[] a) {
    System.out.println(
      new PiWorker().doWork()
    );
  }
}

class PiWorker {

  public int writeDigit(int x) {
    System.out.println(x);
    return x;
  }

  public int doWork() {
    int n;
    int len;
    int i;
    int j;
    int k;
    int q;
    int x;
    int nines;
    int predigit;
    int junk;
    int [] a;

    n = 1000;
    len = 10 * n / 3;
    a = new int[len+1];

    j = 1;
    while (j <= len) {
      a[j] = 2;
      j = j + 1;
    }
    nines = 0;
    predigit = 0;

    j = 1;
    while (j <= n) {
      q = 0;
      i = len;
      while (i >= 1) {
        x = 10*a[i] + q*i;
        a[i] = x % (2*i-1);
        q = x / (2*i-1);
        i = i - 1;
      }
      a[1] = q % 10;
      q = q / 10;

      if (q == 9) {
        nines = nines + 1;
      } else {
        if (q == 10) {
          junk = this.writeDigit(predigit+1);
          k = 1;
          while (k <= nines) {
            junk = this.writeDigit(0);
            k = k + 1;
          }
          predigit = 0;
          nines = 0;
        } else {
          junk = this.writeDigit(predigit);
          predigit = q;
          if (nines != 0) {
            k = 1;
            while (k <= nines) {
              junk = this.writeDigit(9);
              k = k + 1;
            }
            nines = 0;
          } else {
          }
        }
      }
      j = j + 1;
    }
    junk = this.writeDigit(predigit);

    return 111111;
  }

}
