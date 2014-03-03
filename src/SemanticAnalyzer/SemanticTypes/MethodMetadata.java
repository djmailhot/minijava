package SemanticAnalyzer.SemanticTypes;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class MethodMetadata {

  public VarType returnType;
  public LinkedHashMap<String, VarType> params;
  public Map<String, VarType> localVars;
  public ClassVarType enclosingClass;

  // For convenience when printing error messages
  public String name;
  public int lineNumber;

  public MethodMetadata(ClassVarType enclosingClass, VarType returnType, String name, int lineNumber) {
    this.returnType = returnType;
    this.params = new LinkedHashMap<String, VarType>();
    this.localVars = new HashMap<String, VarType>();
    this.name = name;
    this.lineNumber = lineNumber;
    this.enclosingClass = enclosingClass;
  }

  public String toString() {
    return name;
  }
}
