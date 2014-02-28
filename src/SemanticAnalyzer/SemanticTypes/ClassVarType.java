package SemanticAnalyzer.SemanticTypes;

import java.util.LinkedHashMap;
import java.util.HashMap;
import java.util.Map;

public class ClassVarType extends VarType {

  public ClassVarType superclass;
  public Map<String, VarType> fields;
  public Map<String, MethodMetadata> methods;
  public Map<String, Integer> fieldOffsets;
  public Map<String, Integer> methodOffsets;
  private int size;

  // For convenience when printing error messages
  public String name;
  public int lineNumber;

  public ClassVarType(String name, int lineNumber) {
    this.fields = new LinkedHashMap<String, VarType>();
    this.methods = new LinkedHashMap<String, MethodMetadata>();
    this.fieldOffsets = new HashMap<String, Integer>();
    this.methodOffsets = new HashMap<String, Integer>();
    this.size = -1;
    this.name = name;
    this.lineNumber = lineNumber;
  }

  public VarType getFieldType(String name) {
    ClassVarType currentClass = this;
    VarType field = null;

    while (currentClass != null && field == null) {
      field = currentClass.fields.get(name);
      currentClass = currentClass.superclass;
    }

    return field;
  }

  public MethodMetadata getMethod(String name) {
    ClassVarType currentClass = this;
    MethodMetadata method = null;

    while (currentClass != null && method == null) {
      method = currentClass.methods.get(name);
      currentClass = currentClass.superclass;
    }

    return method;
  }

  public int getFieldOffset(String name) {
    ClassVarType currClass = this;
    Integer offset = null;

    while (currClass != null && offset == null) {
      // see if we have it
      if (currClass.fieldOffsets.containsKey(name)) {
        offset = currClass.fieldOffsets.get(name);

      // move to the superclass
      } else {
        currClass = currClass.superclass;
      }
    }
    return offset;
  }

  public int getMethodOffset(String name) {
    ClassVarType currClass = this;
    Integer offset = null;

    while (currClass != null && offset == null) {
      // see if we have it
      if (currClass.methodOffsets.containsKey(name)) {
        offset = currClass.methodOffsets.get(name);

      // move to the superclass
      } else {
        currClass = currClass.superclass;
      }
    }
    return offset;
  }

  public boolean supertypeOrEqual(VarType o) {
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

  public String toString() {
    return name;
  }

  // returns the size in quad words of the class object on the heap in memory
  public int size() {
    return size(this);
  }

  private int size(ClassVarType currClass) {
    int size;
    if (currClass == null) {
      size = 8;  // make room for the vtable
    } else if (currClass.size != -1) {  // size can never be -1 due to the vtable pointer
      size = currClass.size;
    } else {
      size = currClass.fields.size() * 8 + size(currClass.superclass);
      currClass.size = size;  // store the value for later
    }
    return size;
  }

}
