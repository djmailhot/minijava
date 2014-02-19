package SemanticAnalyzer.SemanticTypes;

public class BooleanVarType extends PrimitiveVarType {
  private static BooleanVarType singleton;

  public static BooleanVarType singleton() {
    if (singleton == null) {
      singleton = new BooleanVarType();
    }
    return singleton;
  }

  public String toString() {
    return "boolean";
  }
}
