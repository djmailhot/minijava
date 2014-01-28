//
// Test compile time errors for incompatible array element types
//
class cse401h_ctfail_09 {

  public static void main (String [] args) {
    System.out.println(new TestIncompatibleArrayElementTypes().run());
  }

}

class TestIncompatibleArrayElementTypes {

  public int run() {
    int[] a;

    System.out.println(200000009);

    a = new int[1];
    a[0] = true;

    return a.length;
  }

}
