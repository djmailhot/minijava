package SemanticAnalyzer.SemanticTypes;

public abstract class VarType {

  public abstract boolean subtypeOrEqual(VarType o);

  public boolean supertypeOrEqual(VarType o) {
    return o.subtypeOrEqual(this);
  }

}
