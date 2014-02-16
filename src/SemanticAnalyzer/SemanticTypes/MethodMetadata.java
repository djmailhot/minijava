package SemanticAnalyzer.SemanticTypes;

import java.util.LinkedHashMap;
import java.util.Map;

public class MethodMetadata {

  public VarType returnType;
  public LinkedHashMap<String, VarType> args;

  public MethodMetadata(VarType returnType) {
    this.returnType = returnType;
    this.args = new LinkedHashMap<String, VarType>();
  }
}
