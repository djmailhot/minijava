package SemanticAnalyzer.SemanticTypes;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class MethodMetadata {

  public VarType returnType;
  public LinkedHashMap<String, VarType> args;
  public Map<String, VarType> localVars;

  public MethodMetadata(VarType returnType) {
    this.returnType = returnType;
    this.args = new LinkedHashMap<String, VarType>();
    this.localVars = new HashMap<String, VarType>();
  }

  public String toString() {
    return this.getClass().getName();
  }
}
