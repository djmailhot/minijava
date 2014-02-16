package SemanticAnalyzer.SemanticTypes;

import java.util.HashMap;
import java.util.Map;

public class ClassVarType extends VarType {

  public ClassVarType baseType;  // for subclassing
  public Map<String, VarType> fields;
  public Map<String, MethodMetadata> methods;

  public ClassVarType() {
    this.fields = new HashMap<String, VarType>();
    this.methods = new HashMap<String, MethodMetadata>();
  }

}
