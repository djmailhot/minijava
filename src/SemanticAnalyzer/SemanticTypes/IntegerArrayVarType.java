package SemanticAnalyzer.SemanticTypes;

public class IntegerArrayVarType extends VarType {
  private static IntegerArrayVarType singleton;

  private IntegerArrayVarType() {}

  public static IntegerArrayVarType getIntegerArrayVarType() {
    if (singleton == null) {
      singleton = new IntegerArrayVarType();
    }
    return singleton;
  }

  public String toString() {
    return "int[]";
  }
}
