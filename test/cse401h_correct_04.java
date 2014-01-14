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
    didCauseSideEffect = false;

    if (false && causeSideEffect())
      System.out.println(0);

    if (didCauseSideEffect)
      System.out.println(0);

    didCauseSideEffect = false;

    if (true || causeSideEffect())
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
