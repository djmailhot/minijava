package SemanticAnalyzer.SemanticTypes;

public class DoubleVarType extends VarType {
  private static DoubleVarType singleton;

  private DoubleVarType() {}

  public static DoubleVarType getDoubleVarType() {
    if (singleton == null) {
      singleton = new DoubleVarType();
    }
    return singleton;
  }
}
