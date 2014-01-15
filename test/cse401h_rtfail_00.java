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
    System.out.println(30000000);

    NullType nt;
    nt = null;

    return nt.field;
  }

}

class NullType {
  int field;
}
