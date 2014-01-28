//
// Test runtime errors for dereferencing null pointers
//
class cse401h_rtfail_00 {

  public static void main (String [] args) {
    System.out.println(new TestNullPointer().run());
  }

}

class TestNullPointer {

  public int run() {
    NullType nt;

    System.out.println(300000000);

    nt = null;

    return nt.field;
  }

}

class NullType {
  int field;
}
