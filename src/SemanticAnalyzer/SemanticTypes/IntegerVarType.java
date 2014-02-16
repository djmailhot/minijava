package SemanticAnalyzer.SemanticTypes;

public class IntegerVarType extends VarType {
  private static IntegerVarType singleton;

  private IntegerVarType() {}

  public static IntegerVarType getIntegerVarType() {
    if (singleton == null) {
      singleton = new IntegerVarType();
    }
    return singleton;
  }
}
