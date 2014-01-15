//
// Test compile time errors for dereferencing literals
//  
class cse401h_ctfail_06 {

  public static void main (String [] args) {
    System.out.println(new TestDereferenceLiteral().run());
  }

}

class TestDereferenceLiteral {

  public int run() {
    System.out.println(200000006);

    int x;
    int y;

    x = 0;
    y = x.length;

    return x;
  }

}
