package SemanticAnalyzer.SemanticTypes;

public class IntegerArrayVarType extends PrimitiveVarType {
  private static IntegerArrayVarType singleton;

  public static IntegerArrayVarType singleton() {
    if (singleton == null) {
      singleton = new IntegerArrayVarType();
    }
    return singleton;
  }

  public String toString() {
    return "int[]";
  }
}
