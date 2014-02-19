package SemanticAnalyzer.SemanticTypes;

public class DoubleVarType extends PrimitiveVarType {
  private static DoubleVarType singleton;

  public static DoubleVarType singleton() {
    if (singleton == null) {
      singleton = new DoubleVarType();
    }
    return singleton;
  }

  public String toString() {
    return "double";
  }
}
