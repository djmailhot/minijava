//
// Test function and field overriding
//
class cse401h_correct_06 {

  public static void main(String[] args) {
    System.out.println(new TestOverriding().run());
  }

}

class TestOverriding {

  public int run() {
    Grandparent g;
    Parent p;
    Child c;
    int ret;

    System.out.println(100000006);

    g = new Grandparent();
    ret = g.gInit();

    // Minijava has no super() call, so fake it:
    p = new Parent();
    ret = p.gInit();
    ret = p.pInit();

    c = new Child();
    ret = c.gInit();
    ret = c.pInit();
    ret = c.cInit();

    ret = g.grandparentMethod();

    // p's grandparentMethod prints 1 (Grandparent's field), but p's
    // parentMethod prints 4 for grandparentField, since Parent overrides it.
    ret = p.grandparentMethod();
    ret = p.parentMethod();

    ret = c.grandparentMethod();
    ret = c.parentMethod(); // override; prints 5
    ret = c.childMethod();

    return 0;
  }

}

class Grandparent {
  int grandparentField;

  public int gInit() {
    grandparentField = 1;
    return 0;
  }

  public int grandparentMethod() {
    System.out.println(grandparentField);
    return 0;
  }

}

class Parent extends Grandparent {
  int parentField;
  int grandparentField; // overrides Grandparent's grandparentField

  public int pInit() {
    grandparentField = 4;
    parentField = 2;
    return 0;
  }

  public int parentMethod() {
    System.out.println(grandparentField);
    System.out.println(parentField);
    return 0;
  }

}

class Child extends Parent {
  int childField;

  public int cInit() {
    childField = 3;
    return 0;
  }

  // overrides Parent's parentMethod
  public int parentMethod() {
    System.out.println(5);
    return 0;
  }

  public int childMethod() {
    System.out.println(grandparentField);
    System.out.println(parentField);
    System.out.println(childField);
    return 0;
  }

}
