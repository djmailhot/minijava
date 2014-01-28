//
// Test short circuit AND and OR operators
//
class cse401h_correct_04 {

  public static void main(String[] args) {
    System.out.println(new TestAndOr().run());
  }

}

class TestAndOr {
  boolean didCauseSideEffect;

  public int run() {
    System.out.println(100000004);

    didCauseSideEffect = false;

    if (false && this.causeSideEffect())
      System.out.println(0);

    if (didCauseSideEffect)
      System.out.println(0);

    didCauseSideEffect = false;

    if (true || this.causeSideEffect())
      System.out.println(1);

    if (didCauseSideEffect)
      System.out.println(0);

    return 1;
  }

  public boolean causeSideEffect() {
    didCauseSideEffect = true;
    return true;
  }

}
