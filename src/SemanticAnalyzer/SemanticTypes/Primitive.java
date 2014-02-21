package SemanticAnalyzer.SemanticTypes;

public class Primitive extends VarType {

  public static final IntegerVarType INT = new IntegerVarType();
  public static final DoubleVarType DOUBLE = new DoubleVarType();
  public static final BooleanVarType BOOLEAN = new BooleanVarType();
  public static final IntegerArrayVarType INT_ARRAY = new IntegerArrayVarType();
  public static final DoubleArrayVarType DOUBLE_ARRAY = new DoubleArrayVarType();

  private Primitive() {}

  public boolean supertypeOrEqual(VarType o) {
    return equals(o);
  }

  public static class IntegerVarType extends Primitive {
    private IntegerVarType() {}

    public String toString() {
      return "int";
    }
  }

  public static class DoubleVarType extends Primitive {
    private DoubleVarType() {}

    public String toString() {
      return "double";
    }
  }

  public static class BooleanVarType extends Primitive {
    private BooleanVarType() {}

    public String toString() {
      return "boolean";
    }
  }

  public static class IntegerArrayVarType extends Primitive {
    private IntegerArrayVarType() {}

    public String toString() {
      return "int[]";
    }
  }

  public static class DoubleArrayVarType extends Primitive {
    private DoubleArrayVarType() {}

    public String toString() {
      return "double[]";
    }
  }
}
