package SemanticAnalyzer.SemanticTypes;

public abstract class VarType {

  abstract boolean subtypeOrEqual(VarType o);

  boolean supertypeOrEqual(VarType o) {
    return o.subtypeOrEqual(this);
  }

}
