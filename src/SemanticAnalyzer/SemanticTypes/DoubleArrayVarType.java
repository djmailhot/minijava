package SemanticAnalyzer.SemanticTypes;

public class DoubleArrayVarType extends VarType {
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
