package SemanticAnalyzer.SemanticTypes;

import java.util.HashMap;
import java.util.Map;

public class ClassVarType extends VarType {

  public ClassVarType superclass;
  public Map<String, VarType> fields;
  public Map<String, MethodMetadata> methods;

  // For convenience when printing error messages
  public String name;
  public int lineNumber;

  public ClassVarType(String name, int lineNumber) {
    this.fields = new HashMap<String, VarType>();
    this.methods = new HashMap<String, MethodMetadata>();
    this.name = name;
    this.lineNumber = lineNumber;
  }

  public String toString() {
    return name;
  }

  public boolean subtypeOrEqual(VarType o) {
    if (o instanceof ClassVarType) {
      ClassVarType co = (ClassVarType) o;
      while (co != null) {
        if (equals(co))
          return true;
        co = co.superclass;
      }
    }

    return false;
  }

}
