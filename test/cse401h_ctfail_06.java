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
    int x;
    int y;

    System.out.println(200000006);

    x = 0;
    y = x.length;

    return x;
  }

}
