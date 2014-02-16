package SemanticAnalyzer.SemanticTypes;

import java.util.Map;
import java.util.LinkedHashMap;

public class ProgramMetadata {

  public Map<String, ClassVarType> classes;

  public ProgramMetadata() {
    this.classes = new LinkedHashMap<String, ClassVarType>();
  }

}
