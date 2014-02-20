package SemanticAnalyzer.SemanticTypes;

public abstract class PrimitiveVarType extends VarType {

  public boolean supertypeOrEqual(VarType o) {
    return equals(o);
  }

}
