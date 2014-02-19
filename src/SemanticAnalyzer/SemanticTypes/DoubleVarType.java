package SemanticAnalyzer.SemanticTypes;

public class DoubleVarType extends PrimitiveVarType {
  private static DoubleVarType singleton;

  private DoubleVarType() {}

  public static DoubleVarType getDoubleVarType() {
    if (singleton == null) {
      singleton = new DoubleVarType();
    }
    return singleton;
  }

  public String toString() {
    return "double";
  }
}
