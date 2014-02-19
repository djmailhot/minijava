package SemanticAnalyzer.SemanticTypes;

public class IntegerVarType extends PrimitiveVarType {
  private static IntegerVarType singleton;

  private IntegerVarType() {}

  public static IntegerVarType getIntegerVarType() {
    if (singleton == null) {
      singleton = new IntegerVarType();
    }
    return singleton;
  }

  public String toString() {
    return "int";
  }
}
