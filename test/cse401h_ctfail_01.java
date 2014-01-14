//
// Test compiler errors for the MainClass
//
class cse401h_ctfail_01 {

  public static void main (String [] args) {
    System.out.println(new TestWorker().run());
  }

}

class TestWorker {

  public int run() {
    System.out.println(1);
  }

}
