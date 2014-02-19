package SemanticAnalyzer.SemanticTypes;

public class DoubleArrayVarType extends PrimitiveVarType {
  private static DoubleArrayVarType singleton;

  private DoubleArrayVarType() {}

  public static DoubleArrayVarType getDoubleArrayVarType() {
    if (singleton == null) {
      singleton = new DoubleArrayVarType();
    }
    return singleton;
  }

  public String toString() {
    return "double[]";
  }
}
