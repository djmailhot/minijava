//
// Test compiler errors for the MainClass
//
class cse401h_ctfail_04 {

  public static void main (String [] args) {
    System.out.println(new TestDuplicateClassNames().run());
  }

}

class TestDuplicateClassNames {

  public int run() {
    return 0;
  }

}

class TestDuplicateClassNames {

  public int run() {
    return 1;
  }

}
