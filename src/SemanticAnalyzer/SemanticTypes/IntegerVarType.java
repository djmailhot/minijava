package SemanticAnalyzer.SemanticTypes;

public class IntegerVarType extends PrimitiveVarType {
  private static IntegerVarType singleton;

  public static IntegerVarType singleton() {
    if (singleton == null) {
      singleton = new IntegerVarType();
    }
    return singleton;
  }

  public String toString() {
    return "int";
  }
}
