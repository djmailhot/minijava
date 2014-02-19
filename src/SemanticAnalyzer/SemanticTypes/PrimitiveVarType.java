package SemanticAnalyzer.SemanticTypes;

public abstract class PrimitiveVarType extends VarType {

  public boolean subtypeOrEqual(VarType o) {
    return equals(o);
  }

}
