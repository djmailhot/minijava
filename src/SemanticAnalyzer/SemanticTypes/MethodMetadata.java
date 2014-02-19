package SemanticAnalyzer.SemanticTypes;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class MethodMetadata {

  public VarType returnType;
  public LinkedHashMap<String, VarType> args;
  public Map<String, VarType> localVars;

  // For convenience when printing error messages
  public String name;
  public int lineNumber;

  public MethodMetadata(VarType returnType, String name, int lineNumber) {
    this.returnType = returnType;
    this.args = new LinkedHashMap<String, VarType>();
    this.localVars = new HashMap<String, VarType>();
    this.name = name;
    this.lineNumber = lineNumber;
  }

  public String toString() {
    return name;
  }
}
