package SemanticAnalyzer.SemanticTypes;

public abstract class VarType {

  public boolean subtypeOrEqual(VarType o) {
    return o.supertypeOrEqual(this);
  }

  public abstract boolean supertypeOrEqual(VarType o);

}
