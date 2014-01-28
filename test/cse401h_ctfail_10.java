//
// Test compile time errors for invalid statements
//
class cse401h_ctfail_10 {

  public static void main (String [] args) {
    System.out.println(new TestInvalidStatements().run());
  }

}

class TestInvalidStatements {

  public int run() {
    int[] a;

    System.out.println(20000010);

    a = new int[1];
    a[0] = 1;
    a[0];

    return a.length;
  }

}
