//
// Test object instantiation and inheritance
//
class cse401h_correct_05 {

  public static void main(String[] args) {
    System.out.println(new TestObjects().run());
  }

}

class TestObjects {

  public int run() {
    System.out.println(100000005);

    Grandparent g;
    Parent p;
    Child c;

    g = new Grandparent();
    p = new Parent();
    c = new Child();

    g.grandparentMethod();

    p.grandparentMethod();
    p.parentMethod();

    c.grandparentMethod();
    c.parentMethod();
    c.childMethod();

    return 0;
  }

}

class Grandparent {
  int grandparentField;

  public int grandparentMethod() {
    grandparentField = 1;
    System.out.println(grandparentField);
    return 0;
  }

}

class Parent extends Grandparent {
  int parentField;

  public int parentMethod() {
    parentField = 2;
    System.out.println(grandparentField);
    System.out.println(parentField);
    return 0;
  }

}

class Child extends Parent {
  int childField;

  public int childMethod() {
    childField = 3;
    System.out.println(grandparentField);
    System.out.println(parentField);
    System.out.println(childField);
    return 0;
  }

}
