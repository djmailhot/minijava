package SemanticAnalyzer.SemanticTypes;

public class DoubleArrayVarType extends PrimitiveVarType {
  private static DoubleArrayVarType singleton;

  public static DoubleArrayVarType singleton() {
    if (singleton == null) {
      singleton = new DoubleArrayVarType();
    }
    return singleton;
  }

  public String toString() {
    return "double[]";
  }
}
