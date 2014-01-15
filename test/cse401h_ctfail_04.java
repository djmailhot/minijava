//
// Test compile time errors for duplicate class names
//
class cse401h_ctfail_04 {

  public static void main (String [] args) {
    System.out.println(new TestDuplicateClassNames().run());
  }

}

class TestDuplicateClassNames {

  public int run() {
    System.out.println(20000004);

    return 0;
  }

}

class TestDuplicateClassNames {

  public int run() {
    return 1;
  }

}
