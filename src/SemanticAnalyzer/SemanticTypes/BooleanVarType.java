package SemanticAnalyzer.SemanticTypes;

public class BooleanVarType extends PrimitiveVarType {
  private static BooleanVarType singleton;

  private BooleanVarType() {}

  public static BooleanVarType getBooleanVarType() {
    if (singleton == null) {
      singleton = new BooleanVarType();
    }
    return singleton;
  }

  public String toString() {
    return "boolean";
  }
}
